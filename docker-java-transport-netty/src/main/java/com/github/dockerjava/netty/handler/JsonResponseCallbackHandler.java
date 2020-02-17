package com.github.dockerjava.netty.handler;

import com.github.dockerjava.core.DefaultDockerClientConfig;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.async.ResultCallback;

/**
 * Handler that decodes an incoming byte stream into objects of T and calls {@link ResultCallback#onNext(Object)}
 *
 * @author Marcus Linke
 */
public class JsonResponseCallbackHandler<T> extends SimpleChannelInboundHandler<ByteBuf> {

    private final ObjectMapper objectMapper;

    private TypeReference<T> typeReference;

    private ResultCallback<T> callback;

    @Deprecated
    public JsonResponseCallbackHandler(TypeReference<T> typeReference, ResultCallback<T> callback) {
        this(
                DefaultDockerClientConfig.createDefaultConfigBuilder().build().getObjectMapper(),
                typeReference,
                callback
        );
    }

    public JsonResponseCallbackHandler(ObjectMapper objectMapper, TypeReference<T> typeReference, ResultCallback<T> callback) {
        this.objectMapper = objectMapper;
        this.typeReference = typeReference;
        this.callback = callback;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] buffer = new byte[msg.readableBytes()];
        msg.readBytes(buffer);
        msg.discardReadBytes();

        T object = null;

        try {
            object = objectMapper.readValue(buffer, typeReference);
        } catch (Exception e) {
            callback.onError(e);
            throw new RuntimeException(e);
        }

        callback.onNext(object);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        callback.onError(cause);
        ctx.close();
    }
}
