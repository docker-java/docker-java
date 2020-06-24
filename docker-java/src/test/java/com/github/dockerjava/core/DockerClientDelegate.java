package com.github.dockerjava.core;

import lombok.experimental.Delegate;

public class DockerClientDelegate extends DockerClientImpl {

    @Delegate
    private final DockerClientImpl delegate;

    public DockerClientDelegate(DockerClientImpl delegate) {
        super(DefaultDockerClientConfig.createDefaultConfigBuilder().build());
        this.delegate = delegate;
    }
}
