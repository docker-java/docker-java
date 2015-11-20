package com.github.dockerjava.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.CountDownLatch;

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

    public T await() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return object;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
