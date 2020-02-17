package com.github.dockerjava.core;

public interface DockerClientConfigAware {

    void init(DockerClientConfig dockerClientConfig);
}
