package com.github.dockerjava.transport;

import com.github.dockerjava.okhttp.OkDockerHttpClient;

import java.net.URI;

public class OkHttpClientTests extends DockerHttpClientTCK {

    @Override
    protected DockerHttpClient createDockerHttpClient(URI dockerHost, SSLConfig sslConfig) {
        return new OkDockerHttpClient.Builder()
            .dockerHost(dockerHost)
            .sslConfig(sslConfig)
            .connectTimeout(30 * 100)
            .build();
    }
}
