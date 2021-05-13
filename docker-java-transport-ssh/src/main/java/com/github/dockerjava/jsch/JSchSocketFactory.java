package com.github.dockerjava.jsch;

import com.jcraft.jsch.Session;

import javax.net.SocketFactory;
import java.net.InetAddress;
import java.net.Socket;

public class JSchSocketFactory extends SocketFactory {

    private final Session session;
    private final JschDockerConfig config;

    JSchSocketFactory(Session session, JschDockerConfig config) {
        this.session = session;
        this.config = config;
    }

    @Override
    public Socket createSocket() {
        return new JschSocket(session, config);
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
