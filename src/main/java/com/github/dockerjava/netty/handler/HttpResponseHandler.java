package com.github.dockerjava.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;

import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;

public class HttpResponseHandler extends SimpleChannelInboundHandler<HttpObject> {

    private HttpResponse response;

    private ByteBuf errorBody = Unpooled.buffer();

    private CountDownLatch responseLatch = new CountDownLatch(1);

    private CountDownLatch errorBodyLatch = new CountDownLatch(1);

    public HttpResponseHandler() {
        super(false);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpResponse) {

            response = (HttpResponse) msg;
            responseLatch.countDown();

        } else if (msg instanceof HttpContent) {

            HttpContent content = (HttpContent) msg;

            ByteBuf byteBuf = content.content();

            switch (response.status().code()) {
            case 200:
            case 201:
            case 204:
                ctx.fireChannelRead(byteBuf);
                break;
            default:
                errorBody.writeBytes(byteBuf);
            }

            if (content instanceof LastHttpContent) {
                errorBodyLatch.countDown();
            }
        } else {
            System.err.println("UNKNOWN");
        }

    }

    private String getBodyAsMessage(ByteBuf body) {
        return body.readBytes(body.readableBytes()).toString(Charset.forName("UTF-8"));
    }

    public HttpResponse awaitResponse() {
        try {
            responseLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public String awaitErrorBody() {
        try {
            errorBodyLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return getBodyAsMessage(errorBody);
    }

}
