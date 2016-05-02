package com.github.dockerjava.netty.handler;

import java.util.Arrays;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;

/**
 * Handler that decodes a docker-raw-stream as described here:
 *
 * https://docs.docker.com/engine/reference/api/docker_remote_api_v1.21/#attach-to-a-container
 *
 * It drives the {@link ResultCallback#onNext(Object)} method of the passed {@link ResultCallback}.
 *
 * @author Marcus Linke
 */
public class FramedResponseStreamHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final int HEADER_SIZE = 8;

    private final ByteBuf rawBuffer = Unpooled.buffer(1000);

    private byte[] header = new byte[HEADER_SIZE];

    private int headerCnt = 0;

    private byte[] payload = new byte[0];

    private int payloadCnt = 0;

    private ResultCallback<Frame> resultCallback;

    private StreamType streamType = null;

    public FramedResponseStreamHandler(ResultCallback<Frame> resultCallback) {
        this.resultCallback = resultCallback;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {

        rawBuffer.writeBytes(msg, 0, msg.readableBytes());

        Frame frame = null;

        do {
            frame = decode();

            if (frame != null) {
                resultCallback.onNext(frame);
            }

        } while (frame != null);
    }

    private int read(byte[] buf, int offset, int length) {
        length = Math.min(rawBuffer.readableBytes(), length);
        rawBuffer.readBytes(buf, offset, length);
        rawBuffer.discardReadBytes();
        return length;
    }

    private Frame decode() {
        if (headerCnt < HEADER_SIZE) {

            int headerCount = read(header, headerCnt, HEADER_SIZE - headerCnt);

            if (headerCount == 0) {
                return null;
            }

            headerCnt += headerCount;

            streamType = streamType(header[0]);

            if (streamType.equals(StreamType.RAW)) {
                return new Frame(streamType, Arrays.copyOf(header, headerCount));
            }

            if (headerCnt < HEADER_SIZE) {
                return null;
            }
        }

        if (streamType.equals(StreamType.RAW)) {

            if (payloadCnt == 0) {
                payload = new byte[rawBuffer.readableBytes()];
            }

            int count = read(payload, payloadCnt, rawBuffer.readableBytes());

            if (count == 0) {
                return null;
            }

            payloadCnt = 0;

            return new Frame(StreamType.RAW, payload);
        } else {

            int payloadSize = ((header[4] & 0xff) << 24) + ((header[5] & 0xff) << 16) + ((header[6] & 0xff) << 8)
                    + (header[7] & 0xff);

            if (payloadCnt == 0) {
                payload = new byte[payloadSize];
            }

            int count = read(payload, payloadCnt, payloadSize - payloadCnt);

            if (count == 0) {
                return null;
            }

            payloadCnt += count;

            if (payloadCnt < payloadSize) {
                return null;
            }

            headerCnt = 0;
            payloadCnt = 0;

            return new Frame(streamType, payload);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        resultCallback.onError(cause);
        ctx.close();
    }

    private static StreamType streamType(byte streamType) {
        switch (streamType) {
            case 0:
                return StreamType.STDIN;
            case 1:
                return StreamType.STDOUT;
            case 2:
                return StreamType.STDERR;
            default:
                return StreamType.RAW;
        }
    }

}
