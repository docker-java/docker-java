package com.github.dockerjava.okhttp;

import javax.net.SocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;

class UnixSocketFactory extends SocketFactory {

    private final String socketPath;

    UnixSocketFactory(String socketPath) {
        this.socketPath = socketPath;
    }

    @Override
    public Socket createSocket() {
        try {
            return new UnixDomainSocket(socketPath) {
                @Override
                public void connect(SocketAddress endpoint, int timeout) throws IOException {
                    super.connect(endpoint, timeout);
                }
            };
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Socket createSocket(String s, int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Socket createSocket(String s, int i, InetAddress inetAddress, int i1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Socket createSocket(InetAddress inetAddress, int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress1, int i1) {
        throw new UnsupportedOperationException();
    }
}
