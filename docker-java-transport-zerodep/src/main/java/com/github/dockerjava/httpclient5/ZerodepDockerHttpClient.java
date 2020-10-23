package com.github.dockerjava.httpclient5;

import java.net.URI;
import java.util.Objects;

import com.github.dockerjava.transport.SSLConfig;

@SuppressWarnings("unused")
public final class ZerodepDockerHttpClient extends ApacheDockerHttpClientImpl {

    public static final class Builder {

        private URI dockerHost = null;

        private SSLConfig sslConfig = null;

        private ConnectionPoolConfig connectionPoolConfig = null;

        public Builder dockerHost(URI value) {
            this.dockerHost = Objects.requireNonNull(value, "dockerHost");
            return this;
        }

        public Builder sslConfig(SSLConfig value) {
            this.sslConfig = value;
            return this;
        }

        public Builder connectionPool(ConnectionPoolConfig conf) {
            this.connectionPoolConfig = conf;
            return this;
        }

        public ZerodepDockerHttpClient build() {
            Objects.requireNonNull(dockerHost, "dockerHost");
            return new ZerodepDockerHttpClient(dockerHost, sslConfig, connectionPoolConfig);
        }
    }

    protected ZerodepDockerHttpClient(URI dockerHost, SSLConfig sslConfig, ConnectionPoolConfig connectionPoolConf) {
        super(dockerHost, sslConfig, connectionPoolConf);
    }
}
