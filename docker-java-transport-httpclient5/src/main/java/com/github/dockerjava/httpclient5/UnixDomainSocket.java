/*
 *
 * MariaDB Client for Java
 *
 * Copyright (c) 2012-2014 Monty Program Ab.
 * Copyright (c) 2015-2019 MariaDB Ab.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with this library; if not, write to Monty Program Ab info@montyprogram.com.
 *
 * This particular MariaDB Client for Java file is work
 * derived from a Drizzle-JDBC. Drizzle-JDBC file which is covered by subject to
 * the following copyright and notice provisions:
 *
 * Copyright (c) 2009-2011, Marcus Eriksson
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of the driver nor the names of its contributors may not be
 * used to endorse or promote products derived from this software without specific
 * prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS  AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 *
 */

package com.github.dockerjava.httpclient5;

import com.sun.jna.LastErrorException;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Structure;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

class UnixDomainSocket extends Socket {

    private static final int AF_UNIX = 1;
    private static final int SOCK_STREAM = Platform.isSolaris() ? 2 : 1;
    private static final int PROTOCOL = 0;

    static {
        if (Platform.isSolaris()) {
            System.loadLibrary("nsl");
            System.loadLibrary("socket");
        }
        if (!Platform.isWindows() && !Platform.isWindowsCE()) {
            Native.register("c");
        }
    }

    private final AtomicBoolean closeLock = new AtomicBoolean();
    private final SockAddr sockaddr;
    private final int fd;
    private InputStream is;
    private OutputStream os;
    private boolean connected;

    UnixDomainSocket(String path) throws IOException {
        if (Platform.isWindows() || Platform.isWindowsCE()) {
            throw new IOException("Unix domain sockets are not supported on Windows");
        }
        sockaddr = new SockAddr(path);
        closeLock.set(false);
        try {
            fd = socket(AF_UNIX, SOCK_STREAM, PROTOCOL);
        } catch (LastErrorException lee) {
            throw new IOException("native socket() failed : " + formatError(lee));
        }
    }

    public static native int socket(int domain, int type, int protocol) throws LastErrorException;

    public static native int connect(int sockfd, SockAddr sockaddr, int addrlen)
            throws LastErrorException;

    public static native int read(int fd, byte[] buffer, long size)
            throws LastErrorException;

    public static native int send(int fd, byte[] buffer, int count, int flags)
            throws LastErrorException;

    public static native int close(int fd) throws LastErrorException;

    public static native String strerror(int errno);

    private static String formatError(LastErrorException lee) {
        try {
            return strerror(lee.getErrorCode());
        } catch (Throwable t) {
            return lee.getMessage();
        }
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void close() throws IOException {
        if (!closeLock.getAndSet(true)) {
            try {
                close(fd);
            } catch (LastErrorException lee) {
                throw new IOException("native close() failed : " + formatError(lee));
            }
            connected = false;
        }
    }

    @Override
    public void connect(SocketAddress endpoint) throws IOException {
        connect(endpoint, 0);
    }

    public void connect(SocketAddress endpoint, int timeout) throws IOException {
        try {
            int ret = connect(fd, sockaddr, sockaddr.size());
            if (ret != 0) {
                throw new IOException(strerror(Native.getLastError()));
            }
            connected = true;
        } catch (LastErrorException lee) {
            throw new IOException("native connect() failed : " + formatError(lee));
        }
        is = new UnixSocketInputStream();
        os = new UnixSocketOutputStream();
    }

    public InputStream getInputStream() {
        return is;
    }

    public OutputStream getOutputStream() {
        return os;
    }

    public void setTcpNoDelay(boolean b) {
        // do nothing
    }

    public void setKeepAlive(boolean b) {
        // do nothing
    }

    public void setReceiveBufferSize(int size) {
        // do nothing
    }

    public void setSendBufferSize(int size) {
        // do nothing
    }

    public void setSoLinger(boolean b, int i) {
        // do nothing
    }

    public void setSoTimeout(int timeout) {
    }

    public void shutdownInput() {
        // do nothing
    }

    public void shutdownOutput() {
        // do nothing
    }

    public static class SockAddr extends Structure {

        @SuppressWarnings("checkstyle:membername")
        public short sun_family;
        @SuppressWarnings("checkstyle:membername")
        public byte[] sun_path;

        /**
         * Contructor.
         *
         * @param sunPath path
         */
        SockAddr(String sunPath) {
            sun_family = AF_UNIX;
            byte[] arr = sunPath.getBytes();
            sun_path = new byte[arr.length + 1];
            System.arraycopy(arr, 0, sun_path, 0, Math.min(sun_path.length - 1, arr.length));
            allocateMemory();
        }

        @Override
        protected java.util.List<String> getFieldOrder() {
            return Arrays.asList("sun_family", "sun_path");
        }
    }

    class UnixSocketInputStream extends InputStream {

        @Override
        public int read(byte[] bytesEntry, int off, int len) throws IOException {
            try {
                if (off > 0) {
                    int bytes = 0;
                    int remainingLength = len;
                    int size;
                    byte[] data = new byte[(len < 10240) ? len : 10240];
                    do {
                        if (!isConnected()) {
                            return -1;
                        }
                        size = UnixDomainSocket.read(fd, data, (remainingLength < 10240) ? remainingLength : 10240);
                        if (size <= 0) {
                            return -1;
                        }
                        System.arraycopy(data, 0, bytesEntry, off, size);
                        bytes += size;
                        off += size;
                        remainingLength -= size;
                    } while ((remainingLength > 0) && (size > 0));
                    return bytes;
                } else {
                    if (!isConnected()) {
                        return -1;
                    }
                    int size = UnixDomainSocket.read(fd, bytesEntry, len);
                    if (size <= 0) {
                        return -1;
                    }
                    return size;
                }
            } catch (LastErrorException lee) {
                throw new IOException("native read() failed : " + formatError(lee));
            }
        }

        @Override
        public int read() throws IOException {
            byte[] bytes = new byte[1];
            int bytesRead = read(bytes);
            if (bytesRead == 0) {
                return -1;
            }
            return bytes[0] & 0xff;
        }

        @Override
        public int read(byte[] bytes) throws IOException {
            if (!isConnected()) {
                return -1;
            }
            return read(bytes, 0, bytes.length);
        }
    }

    class UnixSocketOutputStream extends OutputStream {

        @Override
        public void write(byte[] bytesEntry, int off, int len) throws IOException {
            int bytes;
            try {
                if (off > 0) {
                    int size;
                    int remainingLength = len;
                    byte[] data = new byte[(len < 10240) ? len : 10240];
                    do {
                        size = (remainingLength < 10240) ? remainingLength : 10240;
                        System.arraycopy(bytesEntry, off, data, 0, size);
                        if (!isConnected()) {
                            return;
                        }
                        bytes = UnixDomainSocket.send(fd, data, size, 0);
                        if (bytes > 0) {
                            off += bytes;
                            remainingLength -= bytes;
                        }
                    } while ((remainingLength > 0) && (bytes > 0));
                } else {
                    if (!isConnected()) {
                        return;
                    }
                    bytes = UnixDomainSocket.send(fd, bytesEntry, len, 0);
                }

                if (bytes != len) {
                    throw new IOException("can't write " + len + "bytes");
                }
            } catch (LastErrorException lee) {
                throw new IOException("native write() failed : " + formatError(lee));
            }
        }

        @Override
        public void write(int value) throws IOException {
            write(new byte[] {(byte) value});
        }

        @Override
        public void write(byte[] bytes) throws IOException {
            write(bytes, 0, bytes.length);
        }
    }
}
