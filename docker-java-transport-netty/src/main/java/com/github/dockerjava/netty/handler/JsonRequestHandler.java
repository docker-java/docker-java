package com.github.dockerjava.netty.handler;

import com.github.dockerjava.core.DefaultDockerClientConfig;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Handler that encodes an outgoing object to JSON.
 *
 * @author Marcus Linke
 *
 * @deprecated unused in docker-java
 */
@Deprecated
public class JsonRequestHandler extends MessageToByteEncoder<Object> {

    private ObjectMapper mapper = DefaultDockerClientConfig.createDefaultConfigBuilder().build().getObjectMapper();

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        byte[] serialized = mapper.writeValueAsBytes(msg);
        out.writeBytes(serialized);
    }
}
