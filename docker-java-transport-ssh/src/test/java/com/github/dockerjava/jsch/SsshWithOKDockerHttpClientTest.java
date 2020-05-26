package com.github.dockerjava.jsch;

import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.DockerHttpClient;
import com.jcraft.jsch.JSchException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SsshWithOKDockerHttpClientTest {

    @Test
    void ping() throws IOException, JSchException {

        final DefaultDockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
            .withDockerHost("ssh://junit-host")
            .build();

        try (final DockerHttpClient dockerHttpClient = new SsshWithOKDockerHttpClient.Factory().dockerClientConfig(dockerClientConfig).build()) {

            final DockerClientImpl dockerClient = DockerClientImpl.getInstance(dockerClientConfig)
                .withHttpClient(dockerHttpClient);

            assertDoesNotThrow(() -> dockerClient.pingCmd().exec());
        }
    }
}
