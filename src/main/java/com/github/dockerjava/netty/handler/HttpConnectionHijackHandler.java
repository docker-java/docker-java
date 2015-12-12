package com.github.dockerjava.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientUpgradeHandler;
import io.netty.handler.codec.http.HttpRequest;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

public class HttpConnectionHijackHandler implements HttpClientUpgradeHandler.UpgradeCodec {

    private CountDownLatch latch = new CountDownLatch(1);

    private HttpResponseHandler httpResponseHandler;

    public HttpConnectionHijackHandler(HttpResponseHandler httpResponseHandler) {
        this.httpResponseHandler = httpResponseHandler;
    }

    @Override
    public void upgradeTo(ChannelHandlerContext ctx, FullHttpResponse upgradeResponse) throws Exception {
        httpResponseHandler.channelRead(ctx, upgradeResponse);
        ctx.pipeline().addLast(httpResponseHandler);
        latch.countDown();
    }

    @Override
    public Collection<CharSequence> setUpgradeHeaders(ChannelHandlerContext ctx, HttpRequest upgradeRequest) {
        return Collections.emptyList();
    }

    @Override
    public CharSequence protocol() {
        return "tcp";
    }

    public void awaitUpgrade() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
