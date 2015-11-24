package com.github.dockerjava.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.CountDownLatch;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonResponseHandler<T> extends SimpleChannelInboundHandler<ByteBuf> {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private TypeReference<T> typeReference;

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    private T object;

    public JsonResponseHandler(TypeReference<T> typeReference) {
        this.typeReference = typeReference;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.err.println("AwaitObjectInboundHandler: channelRead0: " + msg);
        byte[] buffer = new byte[msg.readableBytes()];
        msg.readBytes(buffer);

        try {
            object = objectMapper.readValue(buffer, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        countDownLatch.countDown();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.err.println("AwaitObjectInboundHandler: channelReadComplete");
        super.channelReadComplete(ctx);
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
