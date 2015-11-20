package com.github.dockerjava.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;

public class FramedResponseStreamHandler extends SimpleChannelInboundHandler<ByteBuf> {

	private static final int HEADER_SIZE = 8;

	private final ByteBuf rawBuffer = Unpooled.buffer(1000);

	private byte[] header = new byte[HEADER_SIZE];

	private int headerCnt = 0;

	private byte[] payload = new byte[0];

	private int payloadCnt = 0;

	private ResultCallback<Frame> resultCallback;

	public FramedResponseStreamHandler(ResultCallback<Frame> resultCallback) {
		this.resultCallback = resultCallback;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {

		rawBuffer.writeBytes(msg.copy(), 0, msg.readableBytes());

		Frame frame = null;

		do {
			frame = decode();

			if (frame != null) {
			    resultCallback.onNext(frame);
				headerCnt = 0;
				payloadCnt = 0;
			}

		} while (frame != null);

	}

	private int read(byte[] buf, int offset, int length) {
		length = Math.min(rawBuffer.readableBytes(), length);
		rawBuffer.readBytes(buf, offset, length);
		return length;
	}

	private Frame decode() {
		if (headerCnt < HEADER_SIZE) {

			int headerCount = read(header, headerCnt, HEADER_SIZE - headerCnt);

			if (headerCount == 0) {
				return null;
			}
			headerCnt += headerCount;

			if (headerCnt < HEADER_SIZE)
				return null;
		}

		StreamType streamType = streamType(header[0]);

		int payloadSize = ((header[4] & 0xff) << 24) + ((header[5] & 0xff) << 16) + ((header[6] & 0xff) << 8)
				+ (header[7] & 0xff);

		if(payloadCnt == 0)
			payload = new byte[payloadSize];

		int count = read(payload, payloadCnt, payloadSize - payloadCnt);

		if (count == 0)
			return null;

		payloadCnt += count;

		if (payloadCnt < payloadSize)
			return null;

		return new Frame(streamType, payload);

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
