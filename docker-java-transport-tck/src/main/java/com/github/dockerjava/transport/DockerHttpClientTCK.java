package com.github.dockerjava.transport;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.transport.DockerHttpClient.Request;
import com.github.dockerjava.transport.DockerHttpClient.Request.Method;
import com.github.dockerjava.transport.DockerHttpClient.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Test;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.dockerclient.TransportConfig;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URI;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class DockerHttpClientTCK {

    protected abstract DockerHttpClient createDockerHttpClient(URI dockerHost, SSLConfig sslConfig);

    @Test
    public void testHijacking() throws Exception {
        try (
            DockerClient client = createDockerClient();

            PipedOutputStream out = new PipedOutputStream();
            PipedInputStream in = new PipedInputStream(out);

            AttachContainerTestCallback callback = new AttachContainerTestCallback();

            AttacheableContainer container = new AttacheableContainer() {
                @Override
                protected void containerIsCreated(String containerId) {
                    client.attachContainerCmd(containerId)
                        .withStdOut(true)
                        .withFollowStream(true)
                        .withStdIn(in)
                        .exec(callback);
                }
            };
        ) {
            container.start();
            assertThat(callback.awaitStarted(5, SECONDS)).as("attached").isTrue();

            String snippet = "hello world";
            out.write((snippet + "\n").getBytes());
            out.flush();

            assertThat(callback.awaitCompletion(15, SECONDS)).as("completed").isTrue();
            assertThat(callback.toString()).contains("STDOUT: " + snippet);
        }
    }

    /**
     * Test that docker-java supports path in DOCKER_HOST
     *
     * @see <a href="https://github.com/moby/moby/blob/7b9275c0da707b030e62c96b679a976f31f929d3/opts/hosts_test.go#L64">valid values</a>
     */
    @Test
    public final void testPath() throws Exception {
        try (MockWebServer server = new MockWebServer()) {
            String dockerHost = server.url("/%20some/path/").toString()
                .replace("http://", "tcp://");

            try (DockerHttpClient client = createDockerHttpClient(dockerHost)) {
                server.enqueue(new MockResponse().setResponseCode(200));
                ping(client);
                assertThat(server.takeRequest().getPath())
                    .as("recorded path")
                    .isEqualTo("/%20some/path/_ping");
            }
        }
    }

    private DockerHttpClient createDockerHttpClient() {
        // Use Testcontainers to detect Docker environment
        TransportConfig transportConfig = DockerClientFactory.instance().getTransportConfig();
        return createDockerHttpClient(transportConfig.getDockerHost(), transportConfig.getSslConfig());
    }

    private DockerHttpClient createDockerHttpClient(String dockerHost) {
        return createDockerHttpClient(URI.create(dockerHost), null);
    }

    private DockerClient createDockerClient() {
        return createDockerClient(createDockerHttpClient());
    }

    private DockerClient createDockerClient(DockerHttpClient dockerHttpClient) {
        return DockerClientImpl.getInstance(
            DefaultDockerClientConfig.createDefaultConfigBuilder().build(),
            dockerHttpClient
        );
    }

    private void ping(DockerHttpClient client) {
        Request pingRequest = Request.builder()
            .method(Method.GET)
            .path("/_ping")
            .build();

        try (Response response = client.execute(pingRequest)) {
            assertThat(response.getStatusCode())
                .as("status code")
                .isEqualTo(200);
        }
    }

    private static class AttachContainerTestCallback extends ResultCallback.Adapter<Frame> {

        private final StringBuffer log = new StringBuffer();

        @Override
        public void onNext(Frame item) {
            log.append(item.toString());
            super.onNext(item);
        }

        @Override
        public String toString() {
            return log.toString();
        }
    }

    private static class AttacheableContainer extends GenericContainer<AttacheableContainer> {

        private AttacheableContainer() {
            super("busybox:1.35.0");

            withCommand("/bin/sh", "-c", "read line && echo $line");
            withCreateContainerCmdModifier(it -> {
                it.withTty(false);
                it.withAttachStdin(true);
                it.withAttachStdout(true);
                it.withAttachStderr(true);
                it.withStdinOpen(true);
            });
        }
    }
}
