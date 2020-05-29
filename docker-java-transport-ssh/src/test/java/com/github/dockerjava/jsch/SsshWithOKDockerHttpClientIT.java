package com.github.dockerjava.jsch;

import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.DockerHttpClient;
import com.jcraft.jsch.JSchException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@EnabledIfEnvironmentVariable(named = "DOCKER_HOST", matches = "ssh://.*")
class SsshWithOKDockerHttpClientIT {

    @Test
    void pingViaDialer() throws IOException, JSchException {

        final DefaultDockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().build();

        try (final DockerHttpClient dockerHttpClient = new SsshWithOKDockerHttpClient.Factory().dockerClientConfig(dockerClientConfig).build()) {

            final DockerClientImpl dockerClient = DockerClientImpl.getInstance(dockerClientConfig)
                .withHttpClient(dockerHttpClient);

            assertDoesNotThrow(() -> dockerClient.pingCmd().exec());
        }
    }

    @Test
    void pingViaSocket() throws IOException, JSchException {

        final DefaultDockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().build();

        try (final DockerHttpClient dockerHttpClient = new SsshWithOKDockerHttpClient.Factory()
            .useSocket()
            .dockerClientConfig(dockerClientConfig).build()) {

            final DockerClientImpl dockerClient = DockerClientImpl.getInstance(dockerClientConfig)
                .withHttpClient(dockerHttpClient);

            assertDoesNotThrow(() -> dockerClient.pingCmd().exec());
        }
    }

    @Test
    void pingViaSocat() throws IOException, JSchException {

        final DefaultDockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().build();

        try (final DockerHttpClient dockerHttpClient = new SsshWithOKDockerHttpClient.Factory()
            .useSocat()
            .dockerClientConfig(dockerClientConfig)
            .build()) {

            final DockerClientImpl dockerClient = DockerClientImpl.getInstance(dockerClientConfig)
                .withHttpClient(dockerHttpClient);

            assertDoesNotThrow(() -> dockerClient.pingCmd().exec());
        }
    }
}
