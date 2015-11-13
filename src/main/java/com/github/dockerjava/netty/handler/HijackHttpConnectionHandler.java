package com.github.dockerjava.netty.handler;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpClientUpgradeHandler;
import io.netty.handler.codec.http.HttpRequest;

public class HijackHttpConnectionHandler implements HttpClientUpgradeHandler.UpgradeCodec {
	
	private CountDownLatch latch = new CountDownLatch(1);
	
	@Override
	public void upgradeTo(ChannelHandlerContext ctx, FullHttpResponse upgradeResponse)
			throws Exception {
		System.out.println("UPGRADED");
		latch.countDown();
//		ChannelHandler removed = ctx.pipeline().remove(HttpClientCodec.class);
//		
//		System.out.println("removed: " + removed);
		
		
		
		

	}

	@Override
	public Collection<CharSequence> setUpgradeHeaders(ChannelHandlerContext ctx,
			HttpRequest upgradeRequest) {
		return Collections.emptyList();
	}

	@Override
	public CharSequence protocol() {
		return "tcp";
	}
	
	public void await() {
		try {
			latch.await();
			System.out.println("upgrade awaited");
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	

}
