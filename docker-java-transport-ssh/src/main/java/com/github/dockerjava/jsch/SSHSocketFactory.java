package com.github.dockerjava.jsch;

import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ContainerPort;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelDirectStreamLocal;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import javax.net.SocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Locale;
import java.util.Objects;

public class SSHSocketFactory extends SocketFactory {

    private final Session session;
    private final SSHDockerConfig config;
    private Container socatContainer;

    SSHSocketFactory(Session session, SSHDockerConfig config) {
        this.session = session;
        this.config = config;
    }

    @Override
    public Socket createSocket() {
        return new Socket() {

            private Channel channel;
            private InputStream inputStream;
            private OutputStream outputStream;

            @Override
            public synchronized void close() throws IOException {
                if (socatContainer != null) {
                    try {
                        SocatHandler.stopSocat(session, socatContainer.getId());
                    } catch (JSchException e) {
                        throw new IOException(e);
                    }
                }
                channel.disconnect();
            }

            @Override
            public void connect(SocketAddress endpoint) throws IOException {
                connect(0);
            }

            @Override
            public void connect(SocketAddress endpoint, int timeout) throws IOException {
                connect(timeout);
            }

            @Override
            public boolean isConnected() {
                return channel.isConnected();
            }

            private void connect(int timeout) throws IOException {
                try {
                    if (config.isUseTcp()) {
                        channel = session.getStreamForwarder("127.0.0.1", config.getTcpPort() != null ? config.getTcpPort() : 2375);
                    } else if (config.isUseSocat() || unixSocketOnWindows()) {
                        // forward docker socket via socat
                        socatContainer = SocatHandler.startSocat(session);
                        final ContainerPort containerPort = socatContainer.getPorts()[0];
                        Objects.requireNonNull(containerPort);
                        channel = session.getStreamForwarder(containerPort.getIp(), containerPort.getPublicPort());
                    } else if (config.isUseSocket()) {
                        // directly forward docker socket
                        channel = session.openChannel("direct-streamlocal@openssh.com");
                        ((ChannelDirectStreamLocal) channel).setSocketPath(config.getSocketPath());
                    } else {
                        // only 18.09 and up
                        channel = session.openChannel("exec");
                        ((ChannelExec) channel).setCommand("docker system dial-stdio");
                    }

                    inputStream = channel.getInputStream();
                    outputStream = channel.getOutputStream();
                    channel.connect(timeout);

                } catch (JSchException e) {
                    throw new IOException(e);
                }
            }

            @Override
            public InputStream getInputStream() {
                return inputStream;
            }

            @Override
            public OutputStream getOutputStream() {
                return outputStream;
            }
        };
    }

    private boolean unixSocketOnWindows() {
        return config.isUseSocket() && config.getSocketPath().equalsIgnoreCase(SSHDockerConfig.VAR_RUN_DOCKER_SOCK) && isWindowsHost();
    }

    private boolean isWindowsHost() {
        final String serverVersion = session.getServerVersion();
        return serverVersion.toLowerCase(Locale.getDefault()).contains("windows");
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
