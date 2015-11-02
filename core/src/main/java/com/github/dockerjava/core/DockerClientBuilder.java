package com.github.dockerjava.core;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.core.DockerClientConfigImpl.DockerClientConfigBuilder;
import com.github.dockerjava.spi.DockerCmdExecFactoryLoader;

public class DockerClientBuilder {

    private DockerClientImpl dockerClient = null;

    private DockerCmdExecFactory dockerCmdExecFactory = null;

    private DockerClientBuilder(DockerClientImpl dockerClient) {
        this.dockerClient = dockerClient;
    }

    public static DockerClientBuilder getInstance() {
        return new DockerClientBuilder(DockerClientImpl.getInstance());
    }

    public static DockerClientBuilder getInstance(DockerClientConfigBuilder dockerClientConfigBuilder) {
        return getInstance(dockerClientConfigBuilder.build());
    }

    public static DockerClientBuilder getInstance(DockerClientConfigImpl dockerClientConfig) {
        return new DockerClientBuilder(DockerClientImpl.getInstance(dockerClientConfig));
    }

    public static DockerClientBuilder getInstance(String serverUrl) {
        return new DockerClientBuilder(DockerClientImpl.getInstance(serverUrl));
    }

    public static DockerCmdExecFactory getDefaultDockerCmdExecFactory() {
        return DockerCmdExecFactoryLoader.loadCommandFactory();
    }

    public DockerClientBuilder withDockerCmdExecFactory(DockerCmdExecFactory dockerCmdExecFactory) {
        this.dockerCmdExecFactory = dockerCmdExecFactory;
        return this;
    }

    public DockerClient build() {
        if (dockerCmdExecFactory != null) {
            dockerClient.withDockerCmdExecFactory(dockerCmdExecFactory);
        } else {
            dockerClient.withDockerCmdExecFactory(getDefaultDockerCmdExecFactory());
        }

        return dockerClient;
    }
}
