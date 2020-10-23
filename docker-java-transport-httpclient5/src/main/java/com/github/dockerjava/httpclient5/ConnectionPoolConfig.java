package com.github.dockerjava.httpclient5;

public class ConnectionPoolConfig {

    private Integer maxConnections;

    public Integer getMaxConnections() {
        return maxConnections;
    }

    public ConnectionPoolConfig setMaxConnections(Integer maxConnections) {
        this.maxConnections = maxConnections;
        return this;
    }

    @Override
    public String toString() {
        return "ConnectionPoolConfig [maxConnections=" + maxConnections + "]";
    }
}
