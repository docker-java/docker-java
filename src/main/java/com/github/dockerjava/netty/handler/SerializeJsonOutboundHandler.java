package com.github.dockerjava.netty.handler;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class SerializeJsonOutboundHandler extends MessageToByteEncoder<Object>{
	
	private ObjectMapper mapper = new ObjectMapper();

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
		byte[] serialized = mapper.writeValueAsBytes(msg);
		System.out.println("serialized: " + new String(serialized));
		out.writeBytes(serialized);
	}
}
