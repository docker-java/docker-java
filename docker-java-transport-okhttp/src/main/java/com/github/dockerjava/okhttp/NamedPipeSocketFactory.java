package com.github.dockerjava.okhttp;

import javax.net.SocketFactory;
import java.net.InetAddress;
import java.net.Socket;

class NamedPipeSocketFactory extends SocketFactory {

    final String socketFileName;

    NamedPipeSocketFactory(String socketFileName) {
        this.socketFileName = socketFileName;
    }

    @Override
    public Socket createSocket() {
        return new NamedPipeSocket(socketFileName);
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
