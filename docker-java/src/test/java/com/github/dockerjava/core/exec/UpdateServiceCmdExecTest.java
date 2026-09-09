package com.github.dockerjava.core.exec;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.ServiceSpec;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.transport.DockerHttpClient;
import com.google.common.io.BaseEncoding;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class UpdateServiceCmdExecTest {

    private static final String REGISTRY_AUTH_HEADER = "X-Registry-Auth";

    @Test
    public void sendsRegistryAuthHeader() throws Exception {
        AtomicReference<DockerHttpClient.Request> request = new AtomicReference<>();
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        DockerClient dockerClient = DockerClientImpl.getInstance(config, new CapturingDockerHttpClient(request));
        AuthConfig authConfig = new AuthConfig()
                .withUsername("user")
                .withPassword("password")
                .withRegistryAddress("registry.example.com");

        dockerClient.updateServiceCmd("service-id", new ServiceSpec())
                .withVersion(1L)
                .withAuthConfig(authConfig)
                .exec();

        String encodedAuth = request.get().headers().get(REGISTRY_AUTH_HEADER);
        assertThat(encodedAuth, notNullValue());
        byte[] decodedAuth = BaseEncoding.base64Url().decode(encodedAuth);
        AuthConfig sentAuth = config.getObjectMapper().readValue(decodedAuth, AuthConfig.class);
        assertThat(sentAuth, is(authConfig));
    }

    @Test
    public void omitsRegistryAuthHeaderWhenNotConfigured() {
        AtomicReference<DockerHttpClient.Request> request = new AtomicReference<>();
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        DockerClient dockerClient = DockerClientImpl.getInstance(config, new CapturingDockerHttpClient(request));

        dockerClient.updateServiceCmd("service-id", new ServiceSpec())
                .withVersion(1L)
                .exec();

        assertThat(request.get().headers().get(REGISTRY_AUTH_HEADER), nullValue());
    }

    private static class CapturingDockerHttpClient implements DockerHttpClient {

        private final AtomicReference<Request> request;

        CapturingDockerHttpClient(AtomicReference<Request> request) {
            this.request = request;
        }

        @Override
        public Response execute(Request request) {
            this.request.set(request);
            return new EmptyResponse();
        }

        @Override
        public void close() throws IOException {
        }
    }

    private static class EmptyResponse implements DockerHttpClient.Response {

        @Override
        public int getStatusCode() {
            return 200;
        }

        @Override
        public Map<String, List<String>> getHeaders() {
            return Collections.emptyMap();
        }

        @Override
        public InputStream getBody() {
            return new ByteArrayInputStream(new byte[0]);
        }

        @Override
        public void close() {
        }
    }
}
