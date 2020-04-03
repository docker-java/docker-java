package com.github.dockerjava.transport.common;

import javax.net.SocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class UnixSocketFactory extends SocketFactory {

    private final String socketPath;

    public UnixSocketFactory(String socketPath) {
        this.socketPath = socketPath;
    }

    @Override
    public Socket createSocket() {
        try {
            return new UnixDomainSocket(socketPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Socket createSocket(String host, int port) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Socket createSocket(InetAddress host, int port) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) {
        throw new UnsupportedOperationException();
    }
}
