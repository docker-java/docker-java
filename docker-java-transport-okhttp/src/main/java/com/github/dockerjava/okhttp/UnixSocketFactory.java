package com.github.dockerjava.okhttp;

import com.github.dockerjava.transport.DomainSocket;

import javax.net.SocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

class UnixSocketFactory extends SocketFactory {

    private final String socketPath;

    UnixSocketFactory(String socketPath) {
        this.socketPath = socketPath;
    }

    @Override
    public Socket createSocket() {
        try {
            return DomainSocket.get(socketPath);
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
