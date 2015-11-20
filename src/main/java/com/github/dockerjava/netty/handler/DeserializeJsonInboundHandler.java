package com.github.dockerjava.netty.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class DeserializeJsonInboundHandler<T> extends SimpleChannelInboundHandler<ByteBuf>{

	private Class<T> clazz;

	public DeserializeJsonInboundHandler(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		byte[] buffer = new byte[msg.readableBytes()];
		msg.readBytes(buffer);

		T object = null;
		try {
			object = new ObjectMapper().readValue(buffer, clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		ctx.fireChannelRead(object);
	}
}
