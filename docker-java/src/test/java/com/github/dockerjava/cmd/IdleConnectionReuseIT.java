package com.github.dockerjava.cmd;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.transport.NamedPipeSocket;
import com.github.dockerjava.transport.UnixSocket;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.github.dockerjava.core.DockerRule.DEFAULT_IMAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assume.assumeTrue;

/**
 * A POST issued after the daemon has closed an idle pooled connection must still succeed.
 *
 * <p>This is the real-daemon counterpart to {@code IdleConnectionReuseTest}, which covers the same
 * behaviour against a fake server over {@code tcp://}. What that test cannot cover is the transports
 * this actually gets reported on — {@code unix://} and {@code npipe://} — so this one runs whatever
 * {@code DOCKER_HOST} points at.
 *
 * <p><strong>It skips against Docker, and that is the point.</strong> The failure needs a daemon that
 * hangs up on idle connections. Dockerd never does, so on Docker this test would pass whether or not
 * the bug is present — a false green. Podman closes after twice its {@code service_timeout} (10s by
 * default), so the bug is reachable there. Rather than guess from the daemon's identity — a podman
 * with {@code service_timeout=0} keeps connections open too — {@link #hangsUpOnIdleConnections}
 * measures the actual behaviour on a raw socket and the test skips when it does not reproduce.
 *
 * <p>Budget roughly {@code docker.java.test.probeMs} (12s) to observe the hang-up plus
 * {@code docker.java.test.idleMs} (15s) to provoke it. Note that failsafe is configured with
 * {@code rerunFailingTestsCount=5}, so a genuine failure is retried; the probe result is cached per
 * JVM so those retries do not re-probe.
 *
 * @see <a href="https://github.com/testcontainers/testcontainers-java/issues/7310">testcontainers-java#7310</a>
 */
public class IdleConnectionReuseIT extends CmdIT {

    public static final Logger LOG = LoggerFactory.getLogger(IdleConnectionReuseIT.class);

    /**
     * How long to provoke the daemon for. Must exceed its idle window; podman's default is 10s.
     * Override with {@code -Ddocker.java.test.idleMs=...} for a daemon tuned differently.
     */
    private static final int IDLE_MS = Integer.getInteger("docker.java.test.idleMs", 15_000);

    /**
     * How long to wait for the hang-up before concluding there is no idle timeout. Kept separate
     * from {@link #IDLE_MS}, and shorter: this is paid on every run, including the runs that go on to
     * skip, so it should not carry the provocation sleep's safety margin.
     */
    private static final int PROBE_TIMEOUT_MS = Integer.getInteger("docker.java.test.probeMs", 12_000);

    private static final String PING_REQUEST =
        "GET /_ping HTTP/1.1\r\n"
            + "Host: localhost\r\n"
            + "Connection: keep-alive\r\n"
            + "\r\n";

    /** Cached so failsafe's reruns, and any further tests here, do not each pay the probe. */
    private static Boolean daemonHangsUp;

    @Test
    public void postSucceedsAfterTheDaemonClosedTheIdleConnection() throws Exception {
        URI dockerHost = dockerRule.getConfig().getDockerHost();

        assumeTrue(
            "the raw-socket probe speaks plaintext HTTP, and DOCKER_HOST is " + dockerHost
                + " with an SSL config present. Skipping conservatively: a non-null SSLConfig is "
                + "taken as TLS in use, which may be stricter than necessary",
            !("tcp".equals(dockerHost.getScheme()) && dockerRule.getConfig().getSSLConfig() != null)
        );

        boolean canHangUp;
        try {
            canHangUp = hangsUpOnIdleConnections(dockerHost);
        } catch (IOException unreachable) {
            // Environmental: the daemon would not talk to us. Skip rather than fail — but note that
            // a bug in the probe itself is a RuntimeException and is deliberately left to propagate,
            // so this test cannot rot into a permanent silent skip.
            LOG.warn("probe against {} could not complete; skipping", dockerHost, unreachable);
            canHangUp = false;
        }
        assumeTrue(
            "this daemon (" + dockerHost + ") kept an idle connection open for " + PROBE_TIMEOUT_MS
                + "ms, so it cannot exhibit the bug — expected for Docker, and for podman with "
                + "service_timeout=0",
            canHangUp
        );

        DockerClient client = dockerRule.getClient();

        // Opens a connection and returns it to the pool.
        client.pingCmd().exec();

        LOG.info("idling {}ms so the daemon closes the pooled connection", IDLE_MS);
        Thread.sleep(IDLE_MS);

        // The non-idempotent request. HttpClient5 does not retry POST, so if the pool hands out the
        // connection the daemon just closed, this is where it surfaces as
        // "NoHttpResponseException: ... failed to respond".
        CreateContainerResponse container = client.createContainerCmd(DEFAULT_IMAGE)
            .withCmd("true")
            .exec();

        assertThat(container.getId(), not(is(emptyString())));
    }

    private static synchronized boolean hangsUpOnIdleConnections(URI dockerHost) throws IOException {
        if (daemonHangsUp == null) {
            daemonHangsUp = probeForHangUp(dockerHost);
        }
        return daemonHangsUp;
    }

    /**
     * Measures the daemon rather than trusting its name: opens a raw connection, completes one
     * request on it, then waits to be hung up on.
     *
     * <p>All of it runs on a worker thread, request included. {@link NamedPipeSocket} and
     * {@link UnixSocket} ignore {@code setSoTimeout} — the same defect that keeps stale-connection
     * validation disabled, see #1726 — so no read here can be bounded in place, and a daemon that
     * accepts a connection without answering would otherwise hang the build indefinitely.
     *
     * @return true if the daemon closed the idle connection within {@link #PROBE_TIMEOUT_MS}
     * @throws IOException if the daemon could not be reached or would not answer
     */
    private static boolean probeForHangUp(URI dockerHost) throws IOException {
        ExecutorService worker = Executors.newSingleThreadExecutor(runnable -> {
            Thread thread = new Thread(runnable, "idle-connection-probe");
            thread.setDaemon(true);
            return thread;
        });
        try (Socket socket = openRawSocket(dockerHost)) {
            Future<Long> hangUp = worker.submit(() -> {
                OutputStream out = socket.getOutputStream();
                out.write(PING_REQUEST.getBytes(StandardCharsets.US_ASCII));
                out.flush();

                InputStream in = socket.getInputStream();
                if (!readPastResponseHeaders(in)) {
                    throw new IOException("daemon closed the connection before answering /_ping");
                }

                long idleSince = System.nanoTime();
                byte[] discard = new byte[256];
                while (in.read(discard) != -1) {
                    // trailing body bytes; keep waiting for the close
                }
                return (System.nanoTime() - idleSince) / 1_000_000L;
            });

            try {
                long afterMs = hangUp.get(PROBE_TIMEOUT_MS, TimeUnit.MILLISECONDS);
                LOG.info("{} hung up on an idle connection after {}ms", dockerHost, afterMs);
                return true;
            } catch (TimeoutException stillOpen) {
                LOG.info("{} kept an idle connection open for {}ms", dockerHost, PROBE_TIMEOUT_MS);
                return false;
            } catch (ExecutionException e) {
                Throwable cause = e.getCause();
                if (cause instanceof IOException) {
                    throw (IOException) cause;
                }
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                }
                throw new IOException(cause);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException(e);
            }
        } finally {
            // The socket is already closed by then, which is what unblocks a read still in flight.
            worker.shutdownNow();
        }
    }

    /**
     * @return false if the connection ended before a complete set of response headers arrived
     */
    private static boolean readPastResponseHeaders(InputStream in) throws IOException {
        ByteArrayOutputStream seen = new ByteArrayOutputStream();
        int c;
        while ((c = in.read()) != -1) {
            seen.write(c);
            if (seen.size() >= 4) {
                byte[] bytes = seen.toByteArray();
                int end = bytes.length;
                if (bytes[end - 4] == '\r' && bytes[end - 3] == '\n'
                    && bytes[end - 2] == '\r' && bytes[end - 1] == '\n') {
                    return true;
                }
            }
        }
        return false;
    }

    private static Socket openRawSocket(URI dockerHost) throws IOException {
        String scheme = dockerHost.getScheme();
        if ("unix".equals(scheme)) {
            return UnixSocket.get(dockerHost.getPath());
        }
        if ("npipe".equals(scheme)) {
            NamedPipeSocket socket = new NamedPipeSocket(dockerHost.getPath());
            // The endpoint is ignored: NamedPipeSocket connects to the path it was constructed with.
            // Passing an unresolved placeholder rather than null, so this does not depend on the
            // override tolerating a null argument that java.net.Socket documents as an NPE.
            socket.connect(InetSocketAddress.createUnresolved("localhost", 0), 10_000);
            return socket;
        }
        if ("tcp".equals(scheme)) {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(dockerHost.getHost(), dockerHost.getPort()), 10_000);
            return socket;
        }
        throw new IOException("unsupported DOCKER_HOST scheme: " + scheme);
    }
}
