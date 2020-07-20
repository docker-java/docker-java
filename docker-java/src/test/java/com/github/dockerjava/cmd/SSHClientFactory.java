package com.github.dockerjava.cmd;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientConfigAware;
import com.github.dockerjava.jsch.JschDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import com.jcraft.jsch.JSchException;

import java.io.IOException;

public class SSHClientFactory implements DockerClientConfigAware {

    private DockerHttpClient httpClient;

    @Override
    public void init(DockerClientConfig dockerClientConfig) {

        final JschDockerHttpClient.Builder builder = new JschDockerHttpClient.Builder()
            .connectTimeout(30 * 1000)
            .dockerHost(dockerClientConfig.getDockerHost());

        final DefaultDockerClientConfig defaultDockerClientConfig = DefaultDockerClientConfig
            .createDefaultConfigBuilder()
            .withRegistryUrl(dockerClientConfig.getRegistryUrl())
            .build();

        if (!"ssh".equalsIgnoreCase(defaultDockerClientConfig.getDockerHost().getScheme())) {
            throw new RuntimeException("This FactoryType is supposed to test ssh connections.");
        }

        if (!dockerClientConfig.getDockerHost().equals(defaultDockerClientConfig.getDockerHost())) {
            // Docker Host was overwritten i.e. from com.github.dockerjava.cmd.swarm.SwarmCmdIT.initializeDockerClient
            // so we still use ssh connection and use inner binding to tcp
            if ("tcp".equalsIgnoreCase(dockerClientConfig.getDockerHost().getScheme())) {
                builder
                    .dockerHost(defaultDockerClientConfig.getDockerHost())
                    .useTcp(dockerClientConfig.getDockerHost().getPort());
            }
        }

        try {
            httpClient = builder.build();
        } catch (IOException | JSchException e) {
            throw new RuntimeException(e);
        }
    }

    SSHClientFactory withDockerClientConfig(DockerClientConfig config) {
        init(config);
        return this;
    }

    public DockerClient build() {

        final DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
            .withRegistryUrl("https://index.docker.io/v1/")
            .build();

        if (httpClient == null) {
            init(config);
        }

        return DockerClientBuilder.getInstance()
            .withDockerHttpClient(
                new TrackingDockerHttpClient(
                    httpClient
                )
            ).build();
    }
}
