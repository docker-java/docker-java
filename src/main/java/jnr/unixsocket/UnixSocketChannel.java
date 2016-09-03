/*
 * Copyright (C) 2009 Wayne Meissner
 *
 * This file is part of the JNR project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jnr.unixsocket;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.nio.channels.UnsupportedAddressTypeException;
import java.util.Set;

import jnr.constants.platform.Errno;
import jnr.constants.platform.ProtocolFamily;
import jnr.constants.platform.Sock;
import jnr.constants.platform.SocketLevel;
import jnr.constants.platform.SocketOption;
import jnr.enxio.channels.NativeSocketChannel;
import jnr.ffi.LastError;
import jnr.ffi.byref.IntByReference;
import scala.Option;

/**
 * A {@link java.nio.channels.Channel} implementation that uses a native unix
 * socket
 */
public class UnixSocketChannel extends NativeSocketChannel {
    enum State {
        UNINITIALIZED, CONNECTED, IDLE, CONNECTING,
    }

    private volatile State state;
    private UnixSocketAddress remoteAddress = null;
    private UnixSocketAddress localAddress = null;

    public static final UnixSocketChannel open() throws IOException {
        return new UnixSocketChannel();
    }

    public static final UnixSocketChannel open(UnixSocketAddress remote) throws IOException {
        UnixSocketChannel channel = new UnixSocketChannel();

        try {
            channel.connect(remote);
        } catch (IOException e) {
            channel.close();
            throw e;
        }
        return channel;
    }

    public static final UnixSocketChannel create() throws IOException {
        UnixSocketChannel channel = new UnixSocketChannel();
        // channel.configureBlocking(true);
        // channel.socket().setKeepAlive(true);
        return channel;
    }

    public static final UnixSocketChannel[] pair() throws IOException {
        int[] sockets = {-1, -1};
        Native.socketpair(ProtocolFamily.PF_UNIX, Sock.SOCK_STREAM, 0, sockets);
        return new UnixSocketChannel[] {new UnixSocketChannel(sockets[0]), new UnixSocketChannel(sockets[1])};
    }

    /**
     * Create a UnixSocketChannel to wrap an existing file descriptor
     * (presumably itself a UNIX socket).
     *
     * @param fd
     *            the file descriptor to wrap
     * @return the new UnixSocketChannel instance
     */
    public static final UnixSocketChannel fromFD(int fd) {
        return fromFD(fd);
    }

    private UnixSocketChannel() throws IOException {
        super(Native.socket(ProtocolFamily.PF_UNIX, Sock.SOCK_STREAM, 0));
        state = State.IDLE;
    }

    UnixSocketChannel(int fd) {
        super(fd);
        state = State.CONNECTED;
    }

    UnixSocketChannel(int fd, UnixSocketAddress remote) {
        super(fd);
        state = State.CONNECTED;
        remoteAddress = remote;
    }

    private boolean doConnect(SockAddrUnix remote) throws IOException {
        if (Native.connect(getFD(), remote, remote.length()) != 0) {
            Errno error = Errno.valueOf(LastError.getLastError(jnr.ffi.Runtime.getSystemRuntime()));

            switch (error) {
            case EAGAIN:
            case EWOULDBLOCK:
                return false;

            default:
                throw new IOException(error.toString());
            }
        }

        // configureBlocking(false);
        return true;
    }

    public boolean connect(UnixSocketAddress remote) throws IOException {
        remoteAddress = remote;
        if (!doConnect(remoteAddress.getStruct())) {

            state = State.CONNECTING;
            return false;

        } else {

            state = State.CONNECTED;
            return true;
        }
    }

    public boolean isConnected() {
        System.out.println("isConnected: " + state);
        return state == State.CONNECTED;
    }

    public boolean isConnectionPending() {
        return state == State.CONNECTING;
    }

    public boolean finishConnect() throws IOException {
        switch (state) {
        case CONNECTED:
            return true;

        case CONNECTING:
            if (!doConnect(remoteAddress.getStruct())) {
                return false;
            }
            state = State.CONNECTED;
            return true;

        default:
            throw new IllegalStateException("socket is not waiting for connect to complete");
        }
    }

    public final UnixSocketAddress getRemoteSocketAddress() {
        if (state != State.CONNECTED) {
            return null;
        }

        if (remoteAddress != null) {
            return remoteAddress;
        } else {
            remoteAddress = getpeername(getFD());
            return remoteAddress;
        }
    }

    public final UnixSocketAddress getLocalSocketAddress() {
        if (state != State.CONNECTED) {
            return null;
        }

        if (localAddress != null) {
            return localAddress;
        } else {
            localAddress = getsockname(getFD());
            return localAddress;
        }
    }

    /**
     * Retrieves the credentials for this UNIX socket. If this socket channel is
     * not in a connected state, this method will return null.
     *
     * See man unix 7; SCM_CREDENTIALS
     *
     * @throws UnsupportedOperationException
     *             if the underlying socket library doesn't support the
     *             SO_PEERCRED option
     *
     * @return the credentials of the remote; null if not connected
     */
    public final Credentials getCredentials() {
        if (state != State.CONNECTED) {
            return null;
        }

        return Credentials.getCredentials(getFD());
    }

    static UnixSocketAddress getpeername(int sockfd) {
        UnixSocketAddress remote = new UnixSocketAddress();
        IntByReference len = new IntByReference(remote.getStruct().getMaximumLength());

        if (Native.libc().getpeername(sockfd, remote.getStruct(), len) < 0) {
            throw new Error(Native.getLastErrorString());
        }

        return remote;
    }

    static UnixSocketAddress getsockname(int sockfd) {
        UnixSocketAddress remote = new UnixSocketAddress();
        IntByReference len = new IntByReference(remote.getStruct().getMaximumLength());

        if (Native.libc().getsockname(sockfd, remote.getStruct(), len) < 0) {
            throw new Error(Native.getLastErrorString());
        }

        return remote;
    }

    public boolean getKeepAlive() {
        int ret = Native.getsockopt(getFD(), SocketLevel.SOL_SOCKET, SocketOption.SO_KEEPALIVE.intValue());
        return (ret == 1) ? true : false;
    }

    public void setKeepAlive(boolean on) {
        Native.setsockopt(getFD(), SocketLevel.SOL_SOCKET, SocketOption.SO_KEEPALIVE, on);
    }

    public int getSoTimeout() {
        return Native.getsockopt(getFD(), SocketLevel.SOL_SOCKET, SocketOption.SO_RCVTIMEO.intValue());
    }

    public void setSoTimeout(int timeout) {
        Native.setsockopt(getFD(), SocketLevel.SOL_SOCKET, SocketOption.SO_RCVTIMEO, timeout);
    }

    @Override
    public SocketAddress getLocalAddress() throws IOException {
        return getLocalSocketAddress();
    }

    @Override
    public <T> T getOption(java.net.SocketOption<T> arg0) throws IOException {
        throw new UnsupportedOperationException("getOption");
    }

    @Override
    public Set<java.net.SocketOption<?>> supportedOptions() {
        throw new UnsupportedOperationException("supportedOptions");
    }

    @Override
    public SocketChannel bind(SocketAddress local) throws IOException {
        throw new UnsupportedOperationException("bind");
    }

    @Override
    public boolean connect(SocketAddress remote) throws IOException {
        if (remote instanceof UnixSocketAddress) {
            return connect(((UnixSocketAddress) remote));
        } else {
            throw new UnsupportedAddressTypeException();
        }
    }

    @Override
    public SocketAddress getRemoteAddress() throws IOException {
        return getRemoteSocketAddress();
    }

    @Override
    public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        throw new UnsupportedOperationException("read");
    }

    @Override
    public <T> SocketChannel setOption(java.net.SocketOption<T> name, T value) throws IOException {
        throw new UnsupportedOperationException("setOption");
    }

    @Override
    public Socket socket() {
        Option<unisockets.SocketChannel> option = Option.apply((unisockets.SocketChannel) null);

        return new unisockets.Socket(this, option);
    }

    @Override
    public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
        throw new UnsupportedOperationException("write");
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        System.out.println("state: " + state);

        if (state == State.CONNECTED) {
            return super.read(dst);
        } else if (state == State.IDLE) {
            return 0;
        } else {
            throw new ClosedChannelException();
        }

    }
}
