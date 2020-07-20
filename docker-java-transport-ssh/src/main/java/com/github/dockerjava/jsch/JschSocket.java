package com.github.dockerjava.jsch;

import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ContainerPort;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelDirectStreamLocal;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Locale;
import java.util.Objects;

import static com.github.dockerjava.jsch.JschDockerHttpClient.OkResponse;

class JschSocket extends Socket {

    private static Logger logger = LoggerFactory.getLogger(JschSocket.class);

    private final JschDockerConfig config;
    private final Session session;

    private Channel channel;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Container socatContainer;

    JschSocket(Session session, JschDockerConfig config) {
        this.session = session;
        this.config = config;
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

    @Override
    public boolean isClosed() {
        return channel != null && channel.isClosed();
    }

    private void connect(int timeout) throws IOException {
        try {
            if (config.isUseTcp()) {
                final int port = config.getTcpPort() != null ? config.getTcpPort() : 2375;
                channel = session.getStreamForwarder("127.0.0.1", port);
                logger.debug("Using channel direct-tcpip with 127.0.0.1:{}", port);
            } else if (config.isUseSocat() || unixSocketOnWindows()) {
                // forward docker socket via socat
                socatContainer = SocatHandler.startSocat(session, config.getSocatFlags());
                final ContainerPort containerPort = socatContainer.getPorts()[0];
                Objects.requireNonNull(containerPort);
                channel = session.getStreamForwarder(containerPort.getIp(), containerPort.getPublicPort());
                logger.debug("Using channel direct-tcpip with socat on port {}", containerPort.getPublicPort());
            } else if (config.isUseSocket()) {
                // directly forward docker socket
                channel = session.openChannel("direct-streamlocal@openssh.com");
                ((ChannelDirectStreamLocal) channel).setSocketPath(config.getSocketPath());
                logger.debug("Using channel direct-streamlocal on {}", config.getSocketPath());
            } else {
                // only 18.09 and up
                channel = session.openChannel("exec");
                ((ChannelExec) channel).setCommand("docker system dial-stdio");
                logger.debug("Using dialer command");
            }

            inputStream = channel.getInputStream();
            outputStream = channel.getOutputStream();

            channel.connect(timeout);

        } catch (JSchException e) {
            throw new IOException(e);
        }
    }

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
    public InputStream getInputStream() {
        return new FilterInputStream(inputStream) {

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                if (Boolean.TRUE.equals(OkResponse.CLOSING.get())) {
                    return 0;
                }

                return super.read(b, off, len);

            }
        };
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }

    private boolean unixSocketOnWindows() {
        return config.isUseSocket() && config.getSocketPath().equalsIgnoreCase(JschDockerConfig.VAR_RUN_DOCKER_SOCK) && isWindowsHost();
    }

    private boolean isWindowsHost() {
        final String serverVersion = session.getServerVersion();
        return serverVersion.toLowerCase(Locale.getDefault()).contains("windows");
    }
}
