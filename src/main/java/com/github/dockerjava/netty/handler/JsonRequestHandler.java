package com.github.dockerjava.netty.handler;

import com.fasterxml.jackson.databind.SerializationFeature;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Handler that encodes an outgoing object to JSON.
 *
 * @author Marcus Linke
 */
public class JsonRequestHandler extends MessageToByteEncoder<Object> {

    private ObjectMapper mapper = new ObjectMapper();

    public JsonRequestHandler() {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        byte[] serialized = mapper.writeValueAsBytes(msg);
        out.writeBytes(serialized);
    }
}
