package com.github.dockerjava.core;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.core.DefaultDockerClientConfig.Builder;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;

public class DockerClientBuilder {

    private DockerClientImpl dockerClient = null;

    private DockerCmdExecFactory dockerCmdExecFactory = null;

    private DockerClientBuilder(DockerClientImpl dockerClient) {
        this.dockerClient = dockerClient;
    }

    public static DockerClientBuilder getInstance() {
        return new DockerClientBuilder(DockerClientImpl.getInstance());
    }

    public static DockerClientBuilder getInstance(Builder dockerClientConfigBuilder) {
        return getInstance(dockerClientConfigBuilder.build());
    }

    public static DockerClientBuilder getInstance(DockerClientConfig dockerClientConfig) {
        return new DockerClientBuilder(DockerClientImpl.getInstance(dockerClientConfig));
    }

    public static DockerClientBuilder getInstance(String serverUrl) {
        return new DockerClientBuilder(DockerClientImpl.getInstance(serverUrl));
    }

    public static DockerCmdExecFactory getDefaultDockerCmdExecFactory() {
        return new JerseyDockerCmdExecFactory();
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
