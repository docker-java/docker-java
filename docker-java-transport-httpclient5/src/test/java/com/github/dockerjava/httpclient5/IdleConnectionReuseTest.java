package com.github.dockerjava.httpclient5;

import com.github.dockerjava.transport.DockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient.Request;
import com.github.dockerjava.transport.DockerHttpClient.Request.Method;
import com.github.dockerjava.transport.DockerHttpClient.Response;
import org.apache.hc.core5.http.NoHttpResponseException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assume.assumeTrue;

/**
 * A pooled connection the daemon closed while it was idle gets handed back out, so the next request
 * is written to a dead socket and no response ever comes back.
 *
 * <p>Reported as {@code NoHttpResponseException: localhost:2375 failed to respond} — the
 * {@code localhost:2375} being the synthetic authority {@link ApacheDockerHttpClientImpl} gives
 * every socket transport, not a TCP endpoint anyone connected to. These tests speak {@code tcp://}
 * to a local server so they stay portable; the pool behaviour under test is transport-agnostic.
 *
 * <p>Two thresholds decide what happens, and every test here is placed relative to them:
 * <ul>
 *   <li>{@link #SERVER_IDLE_MS} — when the daemon drops the connection.</li>
 *   <li>{@link ApacheDockerHttpClientImpl#CONNECTION_KEEP_ALIVE} — when the client gives up on it.
 *       Before the fix this was HttpClient5's 3-minute default, so it was never first.</li>
 * </ul>
 *
 * <p>Whether a request survives a dead connection also depends on its method. HttpClient5 retries
 * idempotent ones, so a GET recovers silently while a POST — {@code /containers/create},
 * {@code /exec} — surfaces the failure. That asymmetry is why the bug reads as random flakiness,
 * and why the reproductions people post are usually a create or an exec.
 *
 * @see <a href="https://github.com/testcontainers/testcontainers-java/issues/7310">testcontainers-java#7310</a>
 * @see <a href="https://github.com/testcontainers/testcontainers-java/issues/7593">testcontainers-java#7593</a>
 */
public class IdleConnectionReuseTest {

    private static final long KEEP_ALIVE_MS = ApacheDockerHttpClientImpl.CONNECTION_KEEP_ALIVE.toMilliseconds();

    /**
     * Deliberately shorter than {@link ApacheDockerHttpClientImpl#CONNECTION_KEEP_ALIVE}, so that
     * the window where the daemon has closed a connection the client still trusts is reachable.
     * Podman's real window is 10s (twice its 5s {@code service_timeout}).
     *
     * <p>Floored well above zero on purpose: this reaches {@code Socket#setSoTimeout}, where zero
     * means <em>infinite</em>, so a bare division would turn a shrunken keep-alive into a server
     * that never drops a connection and tests that hang instead of failing.
     */
    private static final long SERVER_IDLE_MS = Math.max(200, KEEP_ALIVE_MS / 4);

    /** Connection alive at both ends. Derived, so it cannot drift above the window it must sit in. */
    private static final long BEFORE_EITHER_GIVES_UP_MS = SERVER_IDLE_MS / 4;

    /** Daemon has closed it; the client does not know yet. */
    private static final long AFTER_SERVER_BEFORE_CLIENT_MS = (SERVER_IDLE_MS + KEEP_ALIVE_MS) / 2;

    /** Both have given up; the client must open a new one. */
    private static final long AFTER_CLIENT_GIVES_UP_MS = KEEP_ALIVE_MS + 1_000;

    /**
     * Every test here depends on the daemon giving up before the client does. If
     * {@link ApacheDockerHttpClientImpl#CONNECTION_KEEP_ALIVE} were ever lowered past
     * {@link #SERVER_IDLE_MS}, the two windows would cross and the suite would report failures that
     * say nothing about the code. Skip rather than mislead.
     */
    @BeforeClass
    public static void windowsMustNotCross() {
        assumeTrue(
            "server idle window " + SERVER_IDLE_MS + "ms must stay under the client keep-alive of "
                + KEEP_ALIVE_MS + "ms",
            SERVER_IDLE_MS < KEEP_ALIVE_MS
        );
    }

    /**
     * Skip, rather than fail, when the machine stalled long enough to invalidate the timing a test
     * relies on. These tests are placed relative to a 2s production constant, so a GC pause or a
     * loaded CI box between two requests can expire a pooled connection and turn an assertion into a
     * report of a regression that did not happen.
     *
     * <p>Measured from the moment the first request returned, because that is when the connection
     * was released and its expiry clock started
     * ({@code PoolingHttpClientConnectionManager.release} -> {@code updateExpiry}). Starting any
     * earlier would charge the first request's own cost — classloading, opening the connection —
     * against the window, and skip runs that were never actually at risk.
     *
     * @param releasedNanos taken immediately after the first request of the pair returned
     */
    private static void assumeNoStallPastTheKeepAliveWindow(long releasedNanos) {
        long elapsedMs = (System.nanoTime() - releasedNanos) / 1_000_000L;
        assumeTrue(
            "stalled " + elapsedMs + "ms, past the " + KEEP_ALIVE_MS + "ms keep-alive window",
            elapsedMs < KEEP_ALIVE_MS
        );
    }

    /**
     * The control: inside both windows the connection is reused, so a second connection appearing
     * in the tests below means something expired rather than the server refusing to keep any.
     */
    @Test
    public void reusesPooledConnectionWhileBothEndsStillTrustIt() throws Exception {
        try (IdleClosingServer server = new IdleClosingServer();
             DockerHttpClient client = clientFor(server)) {

            execute(client, Method.GET, "/_ping");
            long released = System.nanoTime();
            Thread.sleep(BEFORE_EITHER_GIVES_UP_MS);
            assumeNoStallPastTheKeepAliveWindow(released);
            execute(client, Method.POST, "/containers/create");

            assertThat(server.connectionsAccepted())
                .as("connections accepted — the second request reused the first one")
                .isEqualTo(1);
        }
    }

    /**
     * The bug, and the test a fix has to turn green. Without a keep-alive fallback the client still
     * trusts the connection three seconds after the daemon dropped it, writes the POST into it, and
     * gets nothing back — {@code NoHttpResponseException}. Nothing about the request deserves to
     * fail: the daemon is up and answering, as the other tests show against this same server.
     *
     * <p>The only test here that needs no stall guard: it requires the gap to be <em>longer</em> than
     * the keep-alive window, and a stalled machine can only make it longer still.
     */
    @Test
    public void postSurvivesConnectionThatBothEndsHaveGivenUpOn() throws Exception {
        try (IdleClosingServer server = new IdleClosingServer();
             DockerHttpClient client = clientFor(server)) {

            execute(client, Method.GET, "/_ping");
            Thread.sleep(AFTER_CLIENT_GIVES_UP_MS);
            execute(client, Method.POST, "/containers/create");

            assertThat(server.connectionsAccepted())
                .as("connections accepted — the expired one had to be replaced")
                .isEqualTo(2);
            assertThat(server.bytesWrittenIntoDeadConnections())
                .as("bytes written into the dead connection — the client discarded it up front")
                .isZero();
        }
    }

    /**
     * Inside the keep-alive window the client still hands out the dead connection, and this is the
     * one case where that is survivable: the GET is idempotent, so HttpClient5 retries it on a fresh
     * connection and the caller sees nothing. Guards that recovery against being lost.
     *
     * <p>The sleep has to sit between the two thresholds for this to mean anything. Longer, and the
     * client would discard the connection up front and the retry path would never be entered.
     */
    @Test
    public void getRetriesOntoAFreshConnectionWhenTheDaemonClosedTheIdleOne() throws Exception {
        try (IdleClosingServer server = new IdleClosingServer();
             DockerHttpClient client = clientFor(server)) {

            execute(client, Method.GET, "/_ping");
            long released = System.nanoTime();
            Thread.sleep(AFTER_SERVER_BEFORE_CLIENT_MS);
            assumeNoStallPastTheKeepAliveWindow(released);
            execute(client, Method.GET, "/_ping");

            assertThat(server.bytesWrittenIntoDeadConnections())
                .as("bytes written into the dead connection — proves it was leased out, "
                    + "so the retry is what recovered rather than a proactive replacement")
                .isPositive();
            assertThat(server.connectionsAccepted())
                .as("connections accepted — the retry opened a second one")
                .isEqualTo(2);
        }
    }

    /**
     * The gap the keep-alive fallback does not close, pinned so nobody mistakes it for covered. A
     * connection that dies <em>inside</em> the window — a daemon restart, a relay restart, a network
     * blip, or a peer whose idle timeout is shorter than ours — is still handed out, and a POST on
     * it still fails, because {@code setValidateAfterInactivity(NEG_ONE_SECOND)} disables
     * revalidation on lease and HttpClient5 will not retry a non-idempotent method.
     *
     * <p>Closing it needs either a bounded revalidation (which needs the custom sockets to honour
     * {@code setSoTimeout} first — that is why revalidation was disabled, see #1726) or a retry
     * strategy for {@code NoHttpResponseException} on a reused connection. Delete this test when one
     * of those lands.
     */
    @Test
    public void knownGap_postStillFailsWhenTheConnectionDiesInsideTheKeepAliveWindow() throws Exception {
        try (IdleClosingServer server = new IdleClosingServer();
             DockerHttpClient client = clientFor(server)) {

            execute(client, Method.GET, "/_ping");
            long released = System.nanoTime();
            Thread.sleep(AFTER_SERVER_BEFORE_CLIENT_MS);
            assumeNoStallPastTheKeepAliveWindow(released);

            assertThatThrownBy(() -> execute(client, Method.POST, "/containers/create"))
                .isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(NoHttpResponseException.class)
                .hasMessageContaining("failed to respond");
        }
    }

    private DockerHttpClient clientFor(IdleClosingServer server) {
        String loopback = InetAddress.getLoopbackAddress().getHostAddress();
        return new ApacheDockerHttpClient.Builder()
            .dockerHost(URI.create("tcp://" + loopback + ":" + server.port()))
            .build();
    }

    /**
     * The body has to be read to the end for the connection to reach the pool at all:
     * {@code ApacheResponse.close()} calls {@code request.abort()}, which discards a connection
     * still mid-response. Draining first is what {@code docker-java-core} does on every call, and it
     * is the only reason a connection is ever pooled to be reused — or, here, reused after the
     * daemon has closed it.
     */
    private void execute(DockerHttpClient client, Method method, String path) {
        Request request = Request.builder()
            .method(method)
            .path(path)
            .bodyBytes(method == Method.POST ? "{}".getBytes(StandardCharsets.UTF_8) : null)
            .build();

        try (Response response = client.execute(request)) {
            assertThat(response.getStatusCode()).as("status code of %s %s", method, path).isEqualTo(200);
            drainFully(response.getBody());
        }
    }

    private static void drainFully(InputStream body) {
        try {
            byte[] buffer = new byte[64];
            while (body.read(buffer) != -1) {
                // to the end, so the connection is released to the pool
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * A minimal HTTP/1.1 server standing in for the Docker API socket: keep-alive responses that,
     * like podman, advertise no {@code Keep-Alive} timeout, and a connection dropped once it has sat
     * idle for {@link #SERVER_IDLE_MS}.
     *
     * <p>Dropping it means FIN and then absorbing whatever still arrives, rather than closing
     * outright. A full close makes the peer's next write draw a reset, which is the "broken pipe"
     * shape of this bug; FIN-and-absorb is what the relay in front of the daemon does on Windows and
     * macOS, and it is what produces the reported empty response.
     */
    private static final class IdleClosingServer implements Closeable {

        private static final String RESPONSE = "HTTP/1.1 200 OK\r\nContent-Length: 2\r\n\r\nOK";

        private static final String CONTENT_LENGTH = "Content-Length:";

        /**
         * Applied once a request has started arriving, so a slow or descheduled client mid-message
         * is never mistaken for an idle connection. Only the wait for a request's first byte is
         * bounded by {@link #SERVER_IDLE_MS}.
         */
        private static final int MID_REQUEST_TIMEOUT_MS = 30_000;

        private final ServerSocket serverSocket;

        private final ExecutorService acceptor = Executors.newSingleThreadExecutor(IdleClosingServer::daemon);

        private final ExecutorService connections = Executors.newCachedThreadPool(IdleClosingServer::daemon);

        private final Collection<Socket> open = new ConcurrentLinkedQueue<>();

        private final AtomicInteger accepted = new AtomicInteger();

        private final AtomicInteger absorbedBytes = new AtomicInteger();

        private volatile boolean closed;

        IdleClosingServer() throws IOException {
            serverSocket = new ServerSocket(0, 0, InetAddress.getLoopbackAddress());
            acceptor.submit(this::acceptLoop);
        }

        private static Thread daemon(Runnable runnable) {
            Thread thread = new Thread(runnable, "idle-closing-server");
            thread.setDaemon(true);
            return thread;
        }

        int port() {
            return serverSocket.getLocalPort();
        }

        int connectionsAccepted() {
            return accepted.get();
        }

        /**
         * Bytes the client sent on a connection this server had already given up on. Non-zero means
         * the client leased a dead connection out of its pool; zero means it never did.
         */
        int bytesWrittenIntoDeadConnections() {
            return absorbedBytes.get();
        }

        private void acceptLoop() {
            while (!serverSocket.isClosed()) {
                try {
                    Socket socket = serverSocket.accept();
                    accepted.incrementAndGet();
                    open.add(socket);
                    connections.submit(() -> serve(socket));
                } catch (IOException closedNow) {
                    return;
                }
            }
        }

        private void serve(Socket socket) {
            try {
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                while (readRequest(socket, in)) {
                    // No Keep-Alive header, matching podman: the client is never told when this
                    // connection will go away.
                    out.write(RESPONSE.getBytes(StandardCharsets.US_ASCII));
                    out.flush();
                }
                socket.shutdownOutput();
                absorb(in);
            } catch (IOException hungUp) {
                // nothing useful to do
            } finally {
                // Drop it before closing: close() only needs the sockets still parked in absorb(),
                // and keeping finished ones would grow `open` for the server's whole lifetime.
                open.remove(socket);
                closeQuietly(socket);
            }
        }

        /**
         * @return false once the connection has sat idle for {@link #SERVER_IDLE_MS} without a new
         *         request starting, or the peer has gone — either way it should be dropped
         */
        private boolean readRequest(Socket socket, InputStream in) throws IOException {
            socket.setSoTimeout((int) SERVER_IDLE_MS);
            String requestLine;
            do {
                try {
                    requestLine = readLine(in);
                } catch (SocketTimeoutException idle) {
                    return false;
                }
                if (requestLine == null) {
                    return false;
                }
            } while (requestLine.isEmpty());

            // A request has started; from here a stall is the client being slow, not idle.
            socket.setSoTimeout(MID_REQUEST_TIMEOUT_MS);
            int contentLength = 0;
            for (String line = readLine(in); !"".equals(line); line = readLine(in)) {
                if (line == null) {
                    return false;
                }
                if (line.regionMatches(true, 0, CONTENT_LENGTH, 0, CONTENT_LENGTH.length())) {
                    contentLength = Integer.parseInt(line.substring(CONTENT_LENGTH.length()).trim());
                }
            }
            return drain(in, contentLength);
        }

        private void absorb(InputStream in) throws IOException {
            while (!closed) {
                try {
                    if (in.read() == -1) {
                        return;
                    }
                    // discard, but record: the client is writing into a connection that will never
                    // answer, which is only possible if its pool handed the dead one back out
                    absorbedBytes.incrementAndGet();
                } catch (SocketTimeoutException keepAbsorbing) {
                    // never reset the connection, however long the client takes
                }
            }
        }

        private String readLine(InputStream in) throws IOException {
            StringBuilder line = new StringBuilder();
            int c;
            while ((c = in.read()) != -1) {
                if (c == '\n') {
                    int end = line.length();
                    if (end > 0 && line.charAt(end - 1) == '\r') {
                        line.setLength(end - 1);
                    }
                    return line.toString();
                }
                line.append((char) c);
            }
            return null;
        }

        private boolean drain(InputStream in, int bytes) throws IOException {
            for (int i = 0; i < bytes; i++) {
                if (in.read() == -1) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public void close() {
            closed = true;
            closeQuietly(serverSocket);
            for (Socket socket : open) {
                closeQuietly(socket);
            }
            acceptor.shutdownNow();
            connections.shutdownNow();
        }

        private static void closeQuietly(Closeable closeable) {
            try {
                closeable.close();
            } catch (IOException ignored) {
                // nothing useful to do
            }
        }
    }
}
