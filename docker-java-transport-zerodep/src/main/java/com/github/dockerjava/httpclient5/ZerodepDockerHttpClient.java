package com.github.dockerjava.httpclient5;

import java.net.URI;
import java.time.Duration;
import java.util.Objects;
import com.github.dockerjava.transport.SSLConfig;

@SuppressWarnings("unused")
public final class ZerodepDockerHttpClient extends ApacheDockerHttpClientImpl {

    public static final class Builder {

        private URI dockerHost = null;

        private SSLConfig sslConfig = null;

        private int maxConnections = Integer.MAX_VALUE;

        private Duration connectionTimeout;

        private Duration responseTimeout;

        public Builder dockerHost(URI value) {
            this.dockerHost = Objects.requireNonNull(value, "dockerHost");
            return this;
        }

        public Builder sslConfig(SSLConfig value) {
            this.sslConfig = value;
            return this;
        }

        public Builder maxConnections(int value) {
            this.maxConnections = value;
            return this;
        }

        public Builder connectionTimeout(Duration connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder responseTimeout(Duration responseTimeout) {
            this.responseTimeout = responseTimeout;
            return this;
        }

        public ZerodepDockerHttpClient build() {
            Objects.requireNonNull(dockerHost, "dockerHost");
            return new ZerodepDockerHttpClient(dockerHost, sslConfig, maxConnections, connectionTimeout, responseTimeout);
        }
    }

    private ZerodepDockerHttpClient(URI dockerHost, SSLConfig sslConfig, int maxConnections, Duration connectionTimeout,
        Duration responseTimeout) {
        super(dockerHost, sslConfig, maxConnections, connectionTimeout, responseTimeout);
    }
}
