package com.github.dockerjava.okhttp;

import com.github.dockerjava.okhttp.OkDockerHttpClient.OkResponse;

import javax.net.SocketFactory;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

                @Override
                public InputStream getInputStream() {
                    return new FilterInputStream(super.getInputStream()) {
                        @Override
                        public void close() throws IOException {
                            shutdownInput();
                        }

                        @Override
                        public int read(byte[] b, int off, int len) throws IOException {
                            if (OkResponse.CLOSING.get()) {
                                return 0;
                            }
                            return super.read(b, off, len);
                        }
                    };
                }

                @Override
                public OutputStream getOutputStream() {
                    return new FilterOutputStream(super.getOutputStream()) {

                        @Override
                        public void write(byte[] b, int off, int len) throws IOException {
                            out.write(b, off, len);
                        }

                        @Override
                        public void close() throws IOException {
                            shutdownOutput();
                        }
                    };
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
