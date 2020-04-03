package com.github.dockerjava.httpclient5;

import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.transport.common.UnixSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;

class UnixConnectionSocketFactory extends PlainConnectionSocketFactory {

    private final DockerClientConfig dockerClientConfig;

    UnixConnectionSocketFactory(DockerClientConfig dockerClientConfig) {
        this.dockerClientConfig = dockerClientConfig;
    }

    @Override
    public Socket createSocket(HttpContext httpContext) throws IOException {
        URI dockerHost = dockerClientConfig.getDockerHost();

        return new UnixSocketFactory(dockerHost.getPath()).createSocket();
    }
}
