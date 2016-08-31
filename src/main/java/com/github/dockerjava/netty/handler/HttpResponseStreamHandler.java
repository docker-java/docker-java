package com.github.dockerjava.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;
import java.io.InputStream;

import com.github.dockerjava.api.async.ResultCallback;

/**
 * Handler that converts an incoming byte stream to an {@link InputStream}.
 *
 * @author marcus
 */
public class HttpResponseStreamHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private ResultCallback<InputStream> resultCallback;

    private final HttpResponseInputStream stream = new HttpResponseInputStream();

    public HttpResponseStreamHandler(ResultCallback<InputStream> resultCallback) {
        this.resultCallback = resultCallback;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        invokeCallbackOnFirstRead();

        stream.write(msg.copy());
    }

    private void invokeCallbackOnFirstRead() {
        if (resultCallback != null) {
            resultCallback.onNext(stream);
            resultCallback = null;
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        stream.writeComplete();

        super.channelInactive(ctx);
    }

    public static class HttpResponseInputStream extends InputStream {

        private boolean writeCompleted = false;

        private boolean closed = false;

        private ByteBuf current = null;

        private final Object lock = new Object();

        public void write(ByteBuf byteBuf) throws InterruptedException {
            synchronized (lock) {
                if (closed) {
                    return;
                }
                while (current != null) {
                    lock.wait();

                    if (closed) {
                        return;
                    }
                }
                current = byteBuf;

                lock.notifyAll();
            }
        }

        public void writeComplete() {
            synchronized (lock) {
                writeCompleted = true;

                lock.notifyAll();
            }
        }

        @Override
        public void close() throws IOException {
            synchronized (lock) {
                closed = true;
                releaseCurrent();

                lock.notifyAll();
            }
        }

        @Override
        public int available() throws IOException {
            synchronized (lock) {
                poll(0);
                return readableBytes();
            }
        }

        private int readableBytes() {
            if (current != null) {
                return current.readableBytes();
            } else {
                return 0;
            }
        }

        @Override
        public int read() throws IOException {
            byte[] b = new byte[1];
            int n = read(b, 0, 1);
            return n != -1 ? b[0] : -1;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            synchronized (lock) {
                off = poll(off);

                if (current == null) {
                    return -1;
                } else {
                    int availableBytes = Math.min(len, current.readableBytes() - off);
                    current.readBytes(b, off, availableBytes);
                    return availableBytes;
                }
            }
        }

        private int poll(int off) throws IOException {
            synchronized (lock) {
                while (readableBytes() <= off) {
                    try {
                        if (closed) {
                            throw new IOException("Stream closed");
                        }

                        off -= releaseCurrent();
                        if (writeCompleted) {
                            return off;
                        }
                        while (current == null) {
                            lock.wait();

                            if (closed) {
                                throw new IOException("Stream closed");
                            }
                            if (writeCompleted && current == null) {
                                return off;
                            }
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                return off;
            }
        }

        private int releaseCurrent() {
            synchronized (lock) {
                if (current != null) {
                    int n = current.readableBytes();
                    current.release();
                    current = null;

                    lock.notifyAll();

                    return n;
                }
                return 0;
            }
        }
    }
}
