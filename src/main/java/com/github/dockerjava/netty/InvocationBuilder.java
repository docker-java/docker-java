package com.github.dockerjava.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpClientUpgradeHandler;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.json.JsonObjectDecoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.BadRequestException;
import com.github.dockerjava.api.ConflictException;
import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.InternalServerErrorException;
import com.github.dockerjava.api.NotAcceptableException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.NotModifiedException;
import com.github.dockerjava.api.UnauthorizedException;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.netty.handler.AwaitObjectInboundHandler;
import com.github.dockerjava.netty.handler.DeserializeJsonInboundHandler;
import com.github.dockerjava.netty.handler.FramedResponseStreamHandler;
import com.github.dockerjava.netty.handler.HttpConnectionHijackHandler;
import com.github.dockerjava.netty.handler.HttpRequestProvider;
import com.github.dockerjava.netty.handler.HttpResponseHandler;
import com.github.dockerjava.netty.handler.HttpResponseStreamInboundHandler;

public class InvocationBuilder {

    private ChannelProvider channelProvider;

    private String resource;

    private Map<String, String> headers = new HashMap<String, String>();

    public InvocationBuilder(ChannelProvider channelProvider, String resource) {
        this.channelProvider = channelProvider;
        this.resource = resource;
    }

    public InvocationBuilder accept(MediaType mediaType) {
        headers.put(HttpHeaderNames.ACCEPT.toString(), mediaType.getMediaType());
        return this;
    }

    public <T> T get(Class<T> resultType) {

        HttpRequestProvider requestProvider = httpGetRequestProvider();

        Channel channel = channelProvider.getChannel();

        AwaitObjectInboundHandler<T> awaitObject = new AwaitObjectInboundHandler<T>(resultType);

        HttpResponseHandler responseHandler = new HttpResponseHandler();

        channel.pipeline().addLast(responseHandler);
        channel.pipeline().addLast(new JsonObjectDecoder());
        channel.pipeline().addLast(new DeserializeJsonInboundHandler<T>(resultType));
        channel.pipeline().addLast(awaitObject);

        sendRequestAndHandleResponse(requestProvider, channel, responseHandler);

        return awaitObject.await();

    }

    private void sendRequestAndHandleResponse(HttpRequestProvider requestProvider, Channel channel,
            HttpResponseHandler responseHandler) {

        channel.writeAndFlush(requestProvider.getHttpRequest(resource));

        handleResponse(channel, responseHandler, requestProvider);
    }

    private HttpRequestProvider httpGetRequestProvider() {
        return new HttpRequestProvider() {
            @Override
            public HttpRequest getHttpRequest(String uri) {
                return prepareGetRequest(uri);
            }
        };
    }

    public void get(ResultCallback<Frame> resultCallback) {

        HttpRequestProvider requestProvider = httpGetRequestProvider();

        HttpResponseHandler responseHandler = new HttpResponseHandler();

        FramedResponseStreamHandler streamHandler = new FramedResponseStreamHandler(resultCallback);

        Channel channel = channelProvider.getChannel();

        channel.pipeline().addLast(responseHandler);
        channel.pipeline().addLast(streamHandler);

        sendRequestAndHandleResponse(requestProvider, channel, responseHandler);
    }

    private FullHttpRequest prepareGetRequest(String uri) {

        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri);

        setDefaultHeaders(request);

        return request;
    }

    private void setDefaultHeaders(HttpRequest request) {
        request.headers().set(HttpHeaderNames.HOST, "");
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            request.headers().set((CharSequence) entry.getKey(), entry.getValue());
        }
    }

    public <T> T post(final Object entity, Class<T> resultType) {

        HttpRequestProvider requestProvider = httpPostRequestProvider(entity);

        AwaitObjectInboundHandler<T> awaitObject = new AwaitObjectInboundHandler<T>(resultType);

        Channel channel = channelProvider.getChannel();

        HttpResponseHandler responseHandler = new HttpResponseHandler();

        channel.pipeline().addLast(responseHandler);
        channel.pipeline().addLast(new JsonObjectDecoder());
        channel.pipeline().addLast(new DeserializeJsonInboundHandler<T>(resultType));
        channel.pipeline().addLast(awaitObject);

        sendRequestAndHandleResponse(requestProvider, channel, responseHandler);

        return awaitObject.await();

    }

    public void post(final Object entity, final InputStream stdin, ResultCallback<Frame> resultCallback) {

        HttpRequestProvider requestProvider = httpPostRequestProvider(entity);

        FramedResponseStreamHandler streamHandler = new FramedResponseStreamHandler(resultCallback);

        final Channel channel = channelProvider.getChannel();

        HttpResponseHandler responseHandler = new HttpResponseHandler();

        HttpConnectionHijackHandler hijackHandler = new HttpConnectionHijackHandler(responseHandler);

        channel.pipeline().addLast(
                new HttpClientUpgradeHandler(new HttpClientCodec(), hijackHandler, Integer.MAX_VALUE));
        channel.pipeline().addLast(streamHandler);

        sendRequestAndHandleResponse(requestProvider, channel, responseHandler);

        // wait for successful http upgrade procedure
        hijackHandler.await();

        // now we can start a new thread that reads from stdin and writes to the channel
        new Thread(new Runnable() {

            @Override
            public void run() {

                BufferedReader reader = new BufferedReader(new InputStreamReader(stdin, Charset.forName("UTF-8")));

                int read = -1;
                while ((read = read(reader)) != -1) {
                    byte[] bytes = ByteBuffer.allocate(4).putInt(read).array();
                    try {
                        bytes = new String(bytes).getBytes("US-ASCII");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }

                    channel.writeAndFlush(Unpooled.copiedBuffer(bytes));
                }
            }

            private int read(BufferedReader reader) {
                try {
                    return reader.read();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }

    private void handleResponse(final Channel channel, HttpResponseHandler responseHandler,
            HttpRequestProvider requestProvider) {
        HttpResponse response = responseHandler.awaitResponse();

        switch (response.status().code()) {
        case 101:
        case 200:
        case 201:
        case 204:
            break;
        case 301:
        case 302:
            if (response.headers().contains(HttpHeaderNames.LOCATION)) {
                String location = response.headers().get(HttpHeaderNames.LOCATION);
                System.out.println("redirected to :" + location);
                HttpRequest redirected = requestProvider.getHttpRequest(location);

                channel.writeAndFlush(redirected);
            }
            break;
        case 304:
            throw new NotModifiedException(responseHandler.awaitErrorBody());
        case 400:
            throw new BadRequestException(responseHandler.awaitErrorBody());
        case 401:
            throw new UnauthorizedException(responseHandler.awaitErrorBody());
        case 404:
            throw new NotFoundException(responseHandler.awaitErrorBody());
        case 406:
            throw new NotAcceptableException(responseHandler.awaitErrorBody());
        case 409:
            throw new ConflictException(responseHandler.awaitErrorBody());
        case 500:
            throw new InternalServerErrorException(responseHandler.awaitErrorBody());
        default:
            throw new DockerException(responseHandler.awaitErrorBody(), response.status().code());
        }

    }

    private HttpRequest preparePostRequest(String uri, Object entity) {

        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, uri);

        setDefaultHeaders(request);

        if (entity != null) {
            byte[] bytes;
            try {
                bytes = new ObjectMapper().writeValueAsBytes(entity);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            request.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
            request.content().clear().writeBytes(Unpooled.copiedBuffer(bytes));
            request.headers().set(HttpHeaderNames.CONTENT_LENGTH, bytes.length);
        } else {
            request.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);
        }

        return request;
    }

    private HttpRequest prepareDeleteRequest(String uri) {

        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.DELETE, uri);

        setDefaultHeaders(request);

        return request;
    }

    public InputStream post(final Object entity) {

        HttpRequestProvider requestProvider = httpPostRequestProvider(entity);

        Channel channel = channelProvider.getChannel();

        HttpResponseHandler responseHandler = new HttpResponseHandler();
        HttpResponseStreamInboundHandler streamHandler = new HttpResponseStreamInboundHandler();

        channel.pipeline().addLast(responseHandler);
        channel.pipeline().addLast(streamHandler);

        sendRequestAndHandleResponse(requestProvider, channel, responseHandler);

        return streamHandler.getInputStream();
    }

    private HttpRequestProvider httpPostRequestProvider(final Object entity) {
        return new HttpRequestProvider() {
            @Override
            public HttpRequest getHttpRequest(String uri) {
                return preparePostRequest(uri, entity);
            }
        };
    }

    public void delete() {

        HttpRequestProvider requestProvider = httpDeleteRequestProvider();

        HttpResponseHandler responseHandler = new HttpResponseHandler();

        Channel channel = channelProvider.getChannel();

        channel.pipeline().addLast(responseHandler);

        sendRequestAndHandleResponse(requestProvider, channel, responseHandler);
    }

    private HttpRequestProvider httpDeleteRequestProvider() {
        return new HttpRequestProvider() {
            @Override
            public HttpRequest getHttpRequest(String uri) {
                return prepareDeleteRequest(uri);
            }
        };
    }

    public <T> T get(TypeReference<T> typeReference) {
        Class<T> clazz = (Class<T>) typeReference.getType().getClass();
        return null;
    }
}
