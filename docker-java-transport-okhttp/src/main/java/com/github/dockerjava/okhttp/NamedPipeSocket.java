package com.github.dockerjava.okhttp;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.Channels;
import java.nio.channels.CompletionHandler;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

class NamedPipeSocket extends Socket {

    private final String socketFileName;

    private AsynchronousFileByteChannel channel;

    NamedPipeSocket(String socketFileName) {
        this.socketFileName = socketFileName;
    }

    @Override
    public void close() throws IOException {
        if (channel != null) {
            channel.close();
        }
    }

    @Override
    public void connect(SocketAddress endpoint) throws IOException {
        connect(endpoint, 0);
    }

    @Override
    public void connect(SocketAddress endpoint, int timeout) throws IOException {
        long startedAt = System.currentTimeMillis();

        timeout = Math.max(timeout, 10_000);
        while (true) {
            try {
                channel = new AsynchronousFileByteChannel(
                    AsynchronousFileChannel.open(
                        Paths.get(socketFileName),
                        StandardOpenOption.READ,
                        StandardOpenOption.WRITE
                    )
                );
                break;
            } catch (NoSuchFileException e) {
                if (System.currentTimeMillis() - startedAt >= timeout) {
                    throw new RuntimeException(e);
                } else {
                    Kernel32.INSTANCE.WaitNamedPipe(socketFileName, 100);
                }
            }
        }
    }

    @Override
    public InputStream getInputStream() {
        return Channels.newInputStream(channel);
    }

    @Override
    public OutputStream getOutputStream() {
        return Channels.newOutputStream(channel);
    }

    interface Kernel32 extends StdCallLibrary {

        Kernel32 INSTANCE = Native.load("kernel32", Kernel32.class, W32APIOptions.DEFAULT_OPTIONS);

        @SuppressWarnings("checkstyle:methodname")
        boolean WaitNamedPipe(String lpNamedPipeName, int nTimeOut);
    }

    private static class AsynchronousFileByteChannel implements AsynchronousByteChannel {
        private final AsynchronousFileChannel fileChannel;

        AsynchronousFileByteChannel(AsynchronousFileChannel fileChannel) {
            this.fileChannel = fileChannel;
        }

        @Override
        public <A> void read(ByteBuffer dst, A attachment, CompletionHandler<Integer, ? super A> handler) {
            fileChannel.read(dst, 0, attachment, new CompletionHandler<Integer, A>() {
                @Override
                public void completed(Integer read, A attachment) {
                    handler.completed(read > 0 ? read : -1, attachment);
                }

                @Override
                public void failed(Throwable exc, A attachment) {
                    if (exc instanceof AsynchronousCloseException) {
                        handler.completed(-1, attachment);
                        return;
                    }
                    handler.failed(exc, attachment);
                }
            });
        }

        @Override
        public Future<Integer> read(ByteBuffer dst) {
            CompletableFutureHandler future = new CompletableFutureHandler();
            fileChannel.read(dst, 0, null, future);
            return future;
        }

        @Override
        public <A> void write(ByteBuffer src, A attachment, CompletionHandler<Integer, ? super A> handler) {
            fileChannel.write(src, 0, attachment, handler);
        }

        @Override
        public Future<Integer> write(ByteBuffer src) {
            return fileChannel.write(src, 0);
        }

        @Override
        public void close() throws IOException {
            fileChannel.close();
        }

        @Override
        public boolean isOpen() {
            return fileChannel.isOpen();
        }

        private static class CompletableFutureHandler extends CompletableFuture<Integer> implements CompletionHandler<Integer, Object> {

            @Override
            public void completed(Integer read, Object attachment) {
                complete(read > 0 ? read : -1);
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                if (exc instanceof AsynchronousCloseException) {
                    complete(-1);
                    return;
                }
                completeExceptionally(exc);
            }
        }
    }
}
