package com.github.dockerjava.transport;

import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;

import java.net.URI;

public class HttpClient5Tests extends DockerHttpClientTCK {

    @Override
    protected DockerHttpClient createDockerHttpClient(URI dockerHost, SSLConfig sslConfig) {
        return new ApacheDockerHttpClient.Builder()
            .dockerHost(dockerHost)
            .sslConfig(sslConfig)
            .build();
    }
}
