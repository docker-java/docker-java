package com.github.dockerjava.netty.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.HexDump;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class HttpResponseStreamInboundHandler extends SimpleChannelInboundHandler<ByteBuf> {

	private CountDownLatch latch = new CountDownLatch(1);
	private HttpResponseInputStream stream = new HttpResponseInputStream();

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {

		latch.countDown();
		stream.write(msg.copy());

		//System.out.println("got data: " + msg.readableBytes());

		if (msg.readableBytes() == 0) {			
			stream.close();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	public InputStream getInputStream() {
		try {
			latch.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return stream;
	}

	public static class HttpResponseInputStream extends InputStream {

		private AtomicBoolean closed = new AtomicBoolean(false);

		private LinkedTransferQueue<ByteBuf> queue = new LinkedTransferQueue<ByteBuf>();

		private ByteBuf current = null;

		public void write(ByteBuf byteBuf) {
			queue.put(byteBuf);
		}

		@Override
		public void close() throws IOException {
			closed.set(true);
			super.close();
		}

		@Override
		public int read() throws IOException {
			if (closed.get())
				return -1;

			if (current == null || current.readableBytes() == 0) {
				try {
					current = queue.poll(10, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
			
			if(current != null && current.readableBytes() > 0) {
				return current.readByte();
			} else {
				return read();
			}
			
			

			
		}

	}

}
