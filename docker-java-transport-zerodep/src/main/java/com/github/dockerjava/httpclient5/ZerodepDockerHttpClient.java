package com.github.dockerjava.httpclient5;

import com.github.dockerjava.transport.SSLConfig;

import java.net.URI;
import java.util.Objects;

@SuppressWarnings("unused")
public final class ZerodepDockerHttpClient extends ApacheDockerHttpClientImpl {

    public static final class Builder {

        private URI dockerHost = null;

        private SSLConfig sslConfig = null;

        public Builder dockerHost(URI value) {
            this.dockerHost = Objects.requireNonNull(value, "dockerHost");
            return this;
        }

        public Builder sslConfig(SSLConfig value) {
            this.sslConfig = value;
            return this;
        }

        public ZerodepDockerHttpClient build() {
            Objects.requireNonNull(dockerHost, "dockerHost");
            return new ZerodepDockerHttpClient(dockerHost, sslConfig);
        }
    }

    protected ZerodepDockerHttpClient(URI dockerHost, SSLConfig sslConfig) {
        super(dockerHost, sslConfig);
    }
}
