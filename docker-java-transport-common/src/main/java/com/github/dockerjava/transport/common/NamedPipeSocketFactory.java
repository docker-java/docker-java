package com.github.dockerjava.transport.common;

import com.sun.jna.platform.win32.Kernel32;

import javax.net.SocketFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class NamedPipeSocketFactory extends SocketFactory {

    final String socketFileName;

    public NamedPipeSocketFactory(String socketFileName) {
        this.socketFileName = socketFileName;
    }

    @Override
    public Socket createSocket() {
        return new Socket() {

            RandomAccessFile file;
            InputStream is;
            OutputStream os;

            @Override
            public void close() throws IOException {
                if (file != null) {
                    file.close();
                    file = null;
                }
            }

            @Override
            public void connect(SocketAddress endpoint) {
                connect(endpoint, 0);
            }

            @Override
            public void connect(SocketAddress endpoint, int timeout) {
                long startedAt = System.currentTimeMillis();
                timeout = Math.max(timeout, 10_000);
                while (true) {
                    try {
                        file = new RandomAccessFile(socketFileName, "rw");
                        break;
                    } catch (FileNotFoundException e) {
                        if (System.currentTimeMillis() - startedAt >= timeout) {
                            throw new RuntimeException(e);
                        } else {
                            Kernel32.INSTANCE.WaitNamedPipe(socketFileName, 100);
                        }
                    }
                }

                is = new InputStream() {
                    @Override
                    public int read(byte[] bytes, int off, int len) throws IOException {
                        return file.read(bytes, off, len);
                    }

                    @Override
                    public int read() throws IOException {
                        return file.read();
                    }

                    @Override
                    public int read(byte[] bytes) throws IOException {
                        return file.read(bytes);
                    }
                };

                os = new OutputStream() {
                    @Override
                    public void write(byte[] bytes, int off, int len) throws IOException {
                        file.write(bytes, off, len);
                    }

                    @Override
                    public void write(int value) throws IOException {
                        file.write(value);
                    }

                    @Override
                    public void write(byte[] bytes) throws IOException {
                        file.write(bytes);
                    }
                };
            }

            @Override
            public InputStream getInputStream() {
                return is;
            }

            @Override
            public OutputStream getOutputStream() {
                return os;
            }
        };
    }

    @Override
    public Socket createSocket(String host, int port) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Socket createSocket(InetAddress host, int port) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) {
        throw new UnsupportedOperationException();
    }
}
