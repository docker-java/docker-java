package com.github.dockerjava.httpclient5;

import com.sun.jna.platform.win32.Kernel32;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.SocketAddress;

class NamedPipeSocket extends Socket {

    private final String socketFileName;

    private RandomAccessFile file;

    private InputStream is;

    private OutputStream os;

    NamedPipeSocket(String socketFileName) {
        this.socketFileName = socketFileName;
    }

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
}
