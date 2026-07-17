package com.github.dockerjava.core;

import com.github.dockerjava.transport.DockerHttpClient;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link WslcLifecycleDockerHttpClient} that exercise its request routing and
 * fallback behaviour without a real daemon or a real {@code wslc} CLI. Lifecycle calls are forced
 * to fail deterministically by pointing the decorator at a non-existent executable, which is enough
 * to assert that non-lifecycle traffic is delegated untouched and that failures degrade gracefully.
 */
public class WslcLifecycleDockerHttpClientTest {

    private static final String MISSING_WSLC = "no-such-wslc-binary-xyz";

    /** A recording delegate that returns a fixed response and captures the last request it saw. */
    private static final class RecordingDelegate implements DockerHttpClient {
        final AtomicReference<Request> last = new AtomicReference<>();
        private final int status;
        private final byte[] body;

        RecordingDelegate(int status, String body) {
            this.status = status;
            this.body = body.getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public Response execute(Request request) {
            last.set(request);
            return new Response() {
                @Override public int getStatusCode() {
                    return status;
                }
                @Override public Map<String, List<String>> getHeaders() {
                    return Collections.emptyMap();
                }
                @Override public InputStream getBody() {
                    return new ByteArrayInputStream(body);
                }
                @Override public void close() {
                }
            };
        }

        @Override
        public void close() {
        }
    }

    private static String read(DockerHttpClient.Response response) throws Exception {
        try (InputStream in = response.getBody()) {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n;
            while ((n = in.read(buf)) != -1) {
                bos.write(buf, 0, n);
            }
            return new String(bos.toByteArray(), StandardCharsets.UTF_8);
        }
    }

    @Test
    public void nonLifecycleRequestIsDelegatedUnchanged() throws Exception {
        RecordingDelegate delegate = new RecordingDelegate(200, "[{\"Id\":\"img\"}]");
        WslcLifecycleDockerHttpClient client = new WslcLifecycleDockerHttpClient(delegate, MISSING_WSLC);

        DockerHttpClient.Request req = DockerHttpClient.Request.builder()
            .method(DockerHttpClient.Request.Method.GET)
            .path("/images/json")
            .headers(Collections.emptyMap())
            .build();
        DockerHttpClient.Response resp = client.execute(req);

        assertEquals(200, resp.getStatusCode());
        assertEquals("[{\"Id\":\"img\"}]", read(resp));
        assertNotNull("delegate should have been invoked", delegate.last.get());
        assertEquals("/images/json", delegate.last.get().path());
    }

    @Test
    public void inspectFallsBackToDaemonBodyWhenWslcUnavailable() throws Exception {
        String inspect = "{\"Id\":\"abc123\",\"NetworkSettings\":{\"Ports\":{}}}";
        RecordingDelegate delegate = new RecordingDelegate(200, inspect);
        WslcLifecycleDockerHttpClient client = new WslcLifecycleDockerHttpClient(delegate, MISSING_WSLC);

        DockerHttpClient.Request req = DockerHttpClient.Request.builder()
            .method(DockerHttpClient.Request.Method.GET)
            .path("/containers/abc123/json")
            .headers(Collections.emptyMap())
            .build();
        DockerHttpClient.Response resp = client.execute(req);

        // wslc list cannot run, so the port override is skipped and the daemon's body is returned as-is.
        assertEquals(200, resp.getStatusCode());
        assertEquals(inspect, read(resp));
    }

    @Test
    public void createReturns500WhenWslcCannotRun() throws Exception {
        RecordingDelegate delegate = new RecordingDelegate(201, "{}");
        WslcLifecycleDockerHttpClient client = new WslcLifecycleDockerHttpClient(delegate, MISSING_WSLC);

        DockerHttpClient.Request req = DockerHttpClient.Request.builder()
            .method(DockerHttpClient.Request.Method.POST)
            .path("/containers/create?name=demo")
            .headers(Collections.emptyMap())
            .bodyBytes("{\"Image\":\"busybox\"}".getBytes(StandardCharsets.UTF_8))
            .build();
        DockerHttpClient.Response resp = client.execute(req);

        // The create is routed to `wslc create`; with no wslc on PATH it fails and surfaces as a 500,
        // and it must NOT have been forwarded to the daemon delegate.
        assertEquals(500, resp.getStatusCode());
        assertTrue(read(resp).contains("wslc create failed"));
        assertEquals("delegate must not receive the create", null, delegate.last.get());
    }
}
