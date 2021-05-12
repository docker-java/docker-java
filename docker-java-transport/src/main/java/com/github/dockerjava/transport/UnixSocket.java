package com.github.dockerjava.transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;

public class UnixSocket extends AbstractSocket {
    private final SocketAddress socketAddress;
    private final SocketChannel socketChannel;

    public UnixSocket(SocketAddress address) throws IOException {
        this.socketAddress = address;
        this.socketChannel = SocketChannel.open(address);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (!isConnected()) {
            throw new SocketException("Socket is not connected");
        }
        if (isInputShutdown()) {
            throw new SocketException("Socket input is shutdown");
        }

        return Channels.newInputStream(socketChannel);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (!isConnected()) {
            throw new SocketException("Socket is not connected");
        }
        if (isOutputShutdown()) {
            throw new SocketException("Socket output is shutdown");
        }

        return Channels.newOutputStream(socketChannel);
    }

    @Override
    public SocketAddress getLocalSocketAddress() {
        return socketAddress;
    }

    @Override
    public SocketAddress getRemoteSocketAddress() {
        return socketAddress;
    }

    @Override
    public void close() throws IOException {
        super.close();
        this.socketChannel.close();
    }

    public static Socket get(String dockerHost) throws IOException {
        try {
            Class unixDomainSocketAddress = unixDomainSocketAddress = Class.forName("java.net.UnixDomainSocketAddress");
            SocketAddress address =
                (SocketAddress) unixDomainSocketAddress.getMethod("of", String.class)
                    .invoke(null, dockerHost);
            return new UnixSocket(address);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            return DomainSocket.get(dockerHost);
        }
    }
}
