package com.github.dockerjava.netty.handler;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class AwaitObjectInboundHandler<T> extends SimpleChannelInboundHandler<T> {
	
	public AwaitObjectInboundHandler(Class<T> clazz) {
		super(clazz);
	}
	
	private CountDownLatch countDownLatch = new CountDownLatch(1);
	
	private T object;
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, T msg) throws Exception {
		object = msg;
		countDownLatch.countDown();
	}

	public T await() throws InterruptedException {
		countDownLatch.await();
		return object;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
