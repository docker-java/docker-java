package com.github.dockerjava.transport;

import com.github.dockerjava.jaxrs.JerseyDockerHttpClient;

import java.net.URI;

public class JerseyTests extends DockerHttpClientTCK {

    @Override
    protected DockerHttpClient createDockerHttpClient(URI dockerHost, SSLConfig sslConfig) {
        return new JerseyDockerHttpClient.Builder()
            .dockerHost(dockerHost)
            .sslConfig(sslConfig)
            .connectTimeout(30 * 1000)
            .build();
    }
}
