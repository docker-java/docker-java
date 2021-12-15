package com.github.dockerjava.netty.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.core.DockerClientConfig;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Handler that encodes an outgoing object to JSON.
 *
 * @author Marcus Linke
 *
 * @deprecated unused in docker-java
 */
@Deprecated
public class JsonRequestHandler extends MessageToByteEncoder<Object> {

    private ObjectMapper mapper = DockerClientConfig.getDefaultObjectMapper();

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        byte[] serialized = mapper.writeValueAsBytes(msg);
        out.writeBytes(serialized);
    }
}
