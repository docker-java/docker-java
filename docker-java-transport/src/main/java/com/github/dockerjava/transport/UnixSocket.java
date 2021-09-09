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

    /**
     * Return a new {@link Socket} for the given path. Will use JDK's {@link java.net.UnixDomainSocketAddress}
     * if available and fallback to {@link DomainSocket} otherwise.
     *
     * @param path the path to the domain socket
     * @return a {@link Socket} instance
     * @throws IOException if the socket cannot be opened
     */
    public static Socket get(String path) throws IOException {
        try {
            return new UnixSocket(path);
        } catch (Exception e) {
            //noinspection deprecation
            return DomainSocket.get(path);
        }
    }

    private final SocketAddress socketAddress;

    private final SocketChannel socketChannel;

    private UnixSocket(String path) throws Exception {
        Class<?> unixDomainSocketAddress = Class.forName("java.net.UnixDomainSocketAddress");
        this.socketAddress =
            (SocketAddress) unixDomainSocketAddress.getMethod("of", String.class)
                .invoke(null, path);
        this.socketChannel = SocketChannel.open(this.socketAddress);
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
}
