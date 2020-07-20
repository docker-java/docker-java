package com.github.dockerjava.jsch;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.transport.DockerHttpClient;
import com.jcraft.jsch.JSchException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@EnabledIfEnvironmentVariable(named = "DOCKER_HOST", matches = "ssh://.*")
class JschDockerHttpClientIT {

    @Test
    void pingViaDialer() throws IOException, JSchException {

        final DefaultDockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().build();

        try (final DockerHttpClient dockerHttpClient = new JschDockerHttpClient.Builder()
            .dockerHost(dockerClientConfig.getDockerHost()).build()) {

            final DockerClient dockerClient = DockerClientImpl.getInstance(dockerClientConfig, dockerHttpClient);

            assertDoesNotThrow(() -> dockerClient.pingCmd().exec());
        }
    }

    @Test
    void pingViaSocket() throws IOException, JSchException {

        final DefaultDockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().build();

        try (final DockerHttpClient dockerHttpClient = new JschDockerHttpClient.Builder()
            .useSocket()
            .dockerHost(dockerClientConfig.getDockerHost())
            .build()) {

            final DockerClient dockerClient = DockerClientImpl.getInstance(dockerClientConfig, dockerHttpClient);

            assertDoesNotThrow(() -> dockerClient.pingCmd().exec());
        }
    }

    @Test
    void pingViaSocat() throws IOException, JSchException {

        final DefaultDockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().build();

        try (final DockerHttpClient dockerHttpClient = new JschDockerHttpClient.Builder()
            .useSocat()
            .dockerHost(dockerClientConfig.getDockerHost())
            .build()) {

            final DockerClient dockerClient = DockerClientImpl.getInstance(dockerClientConfig, dockerHttpClient);

            assertDoesNotThrow(() -> dockerClient.pingCmd().exec());
        }
    }
}
