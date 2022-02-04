package com.github.dockerjava.transport;

import com.github.dockerjava.transport.DockerHttpClient.Request;
import com.github.dockerjava.transport.DockerHttpClient.Request.Method;
import com.github.dockerjava.transport.DockerHttpClient.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Test;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class DockerHttpClientTCK {

    protected abstract DockerHttpClient createDockerHttpClient(URI dockerHost, SSLConfig sslConfig);

    /**
     * Test that docker-java supports path in DOCKER_HOST
     *
     * @see <a href="https://github.com/moby/moby/blob/7b9275c0da707b030e62c96b679a976f31f929d3/opts/hosts_test.go#L64">valid values</a>
     */
    @Test
    public final void testPath() throws Exception {
        try (MockWebServer server = new MockWebServer()) {
            String dockerHost = server.url("/%20path/").toString()
                .replace("http://", "tcp://");

            try (DockerHttpClient client = createDockerHttpClient(dockerHost)) {
                server.enqueue(new MockResponse().setResponseCode(200));
                ping(client);
                assertThat(server.takeRequest().getPath())
                    .as("recorded path")
                    .isEqualTo("/%20path/_ping");
            }
        }
    }

    private DockerHttpClient createDockerHttpClient(String dockerHost) {
        return createDockerHttpClient(URI.create(dockerHost), null);
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
}
