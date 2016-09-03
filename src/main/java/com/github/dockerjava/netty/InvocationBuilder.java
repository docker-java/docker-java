package com.github.dockerjava.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.DuplexChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpClientUpgradeHandler;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.stream.ChunkedStream;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.async.ResultCallbackTemplate;
import com.github.dockerjava.netty.handler.FramedResponseStreamHandler;
import com.github.dockerjava.netty.handler.HttpConnectionHijackHandler;
import com.github.dockerjava.netty.handler.HttpRequestProvider;
import com.github.dockerjava.netty.handler.HttpResponseHandler;
import com.github.dockerjava.netty.handler.HttpResponseStreamHandler;
import com.github.dockerjava.netty.handler.JsonResponseCallbackHandler;

/**
 * This class is basically a replacement of javax.ws.rs.client.Invocation.Builder to allow simpler migration of JAX-RS code to a netty based
 * implementation.
 *
 * @author Marcus Linke
 */
public class InvocationBuilder {

    public class ResponseCallback<T> extends ResultCallbackTemplate<ResponseCallback<T>, T> {

        private T result = null;

        public T awaitResult() {
            try {
                awaitCompletion();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return result;
        }

        @Override
        public void onNext(T object) {
            result = object;
        }
    }

    public class SkipResultCallback extends ResultCallbackTemplate<ResponseCallback<Void>, Void> {
        @Override
        public void onNext(Void object) {
        }
    }

    /**
     * Implementation of {@link ResultCallback} with the single result event expected.
     */
    public static class AsyncResultCallback<A_RES_T>
            extends ResultCallbackTemplate<AsyncResultCallback<A_RES_T>, A_RES_T> {

        private A_RES_T result = null;

        private final CountDownLatch resultReady = new CountDownLatch(1);

        @Override
        public void onNext(A_RES_T object) {
            onResult(object);
        }

        private void onResult(A_RES_T object) {
            if (resultReady.getCount() == 0) {
                throw new IllegalStateException("Result has already been set");
            }

            try {
                result = object;
            } finally {
                resultReady.countDown();
            }
        }

        @Override
        public void close() throws IOException {
            try {
                super.close();
            } finally {
                resultReady.countDown();
            }
        }

        /**
         * Blocks until {@link ResultCallback#onNext(Object)} was called for the first time
         */
        @SuppressWarnings("unchecked")
        public A_RES_T awaitResult() {
            try {
                resultReady.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            getFirstError();
            return result;
        }
    }

    private ChannelProvider channelProvider;

    private String resource;

    private Map<String, String> headers = new HashMap<String, String>();

    public InvocationBuilder(ChannelProvider channelProvider, String resource) {
        this.channelProvider = channelProvider;
        this.resource = resource;
    }

    public InvocationBuilder accept(MediaType mediaType) {
        return header(HttpHeaderNames.ACCEPT.toString(), mediaType.getMediaType());
    }

    public InvocationBuilder header(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public void delete() {

        HttpRequestProvider requestProvider = httpDeleteRequestProvider();

        ResponseCallback<Void> callback = new ResponseCallback<Void>();

        HttpResponseHandler responseHandler = new HttpResponseHandler(requestProvider, callback);

        Channel channel = getChannel();

        channel.pipeline().addLast(responseHandler);

        sendRequest(requestProvider, channel);

        callback.awaitResult();
    }

    public void get(ResultCallback<Frame> resultCallback) {

        HttpRequestProvider requestProvider = httpGetRequestProvider();

        HttpResponseHandler responseHandler = new HttpResponseHandler(requestProvider, resultCallback);

        FramedResponseStreamHandler streamHandler = new FramedResponseStreamHandler(resultCallback);

        Channel channel = getChannel();

        channel.pipeline().addLast(responseHandler);
        channel.pipeline().addLast(streamHandler);

        sendRequest(requestProvider, channel);
    }

    public <T> T get(TypeReference<T> typeReference) {

        ResponseCallback<T> callback = new ResponseCallback<T>();

        get(typeReference, callback);

        return callback.awaitResult();
    }

    public <T> void get(TypeReference<T> typeReference, ResultCallback<T> resultCallback) {

        HttpRequestProvider requestProvider = httpGetRequestProvider();

        Channel channel = getChannel();

        JsonResponseCallbackHandler<T> jsonResponseHandler = new JsonResponseCallbackHandler<T>(typeReference,
                resultCallback);

        HttpResponseHandler responseHandler = new HttpResponseHandler(requestProvider, resultCallback);

        channel.pipeline().addLast(responseHandler);
        channel.pipeline().addLast(new JsonObjectDecoder());
        channel.pipeline().addLast(jsonResponseHandler);

        sendRequest(requestProvider, channel);

        return;
    }

    private DuplexChannel getChannel() {
        return channelProvider.getChannel();
    }

    private HttpRequestProvider httpDeleteRequestProvider() {
        return new HttpRequestProvider() {
            @Override
            public HttpRequest getHttpRequest(String uri) {
                return prepareDeleteRequest(uri);
            }
        };
    }

    private HttpRequestProvider httpGetRequestProvider() {
        return new HttpRequestProvider() {
            @Override
            public HttpRequest getHttpRequest(String uri) {
                return prepareGetRequest(uri);
            }
        };
    }

    private HttpRequestProvider httpPostRequestProvider(final Object entity) {
        return new HttpRequestProvider() {
            @Override
            public HttpRequest getHttpRequest(String uri) {
                return preparePostRequest(uri, entity);
            }
        };
    }

    private HttpRequestProvider httpPutRequestProvider(final Object entity) {
        return new HttpRequestProvider() {
            @Override
            public HttpRequest getHttpRequest(String uri) {
                return preparePutRequest(uri, entity);
            }
        };
    }

    public InputStream post(final Object entity) {

        HttpRequestProvider requestProvider = httpPostRequestProvider(entity);

        Channel channel = getChannel();

        AsyncResultCallback<InputStream> callback = new AsyncResultCallback<>();

        HttpResponseHandler responseHandler = new HttpResponseHandler(requestProvider, callback);
        HttpResponseStreamHandler streamHandler = new HttpResponseStreamHandler(callback);

        channel.pipeline().addLast(responseHandler);
        channel.pipeline().addLast(streamHandler);

        sendRequest(requestProvider, channel);

        return callback.awaitResult();
    }

    public void post(final Object entity, final InputStream stdin, final ResultCallback<Frame> resultCallback) {

        HttpRequestProvider requestProvider = httpPostRequestProvider(entity);

        FramedResponseStreamHandler streamHandler = new FramedResponseStreamHandler(resultCallback);

        final DuplexChannel channel = getChannel();

        // result callback's close() method must be called when the servers closes the connection
        channel.closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                resultCallback.onComplete();
            }
        });

        HttpResponseHandler responseHandler = new HttpResponseHandler(requestProvider, resultCallback);

        HttpConnectionHijackHandler hijackHandler = new HttpConnectionHijackHandler(responseHandler);

        HttpClientCodec httpClientCodec = channel.pipeline().get(HttpClientCodec.class);

        channel.pipeline().addLast(
                new HttpClientUpgradeHandler(httpClientCodec, hijackHandler, Integer.MAX_VALUE));
        channel.pipeline().addLast(streamHandler);

        sendRequest(requestProvider, channel);

        // wait for successful http upgrade procedure
        hijackHandler.awaitUpgrade();

        if (stdin != null) {
            // now we can start a new thread that reads from stdin and writes to the channel
            new Thread(new Runnable() {

                private int read(InputStream is, byte[] buf) {
                    try {
                        return is.read(buf);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void run() {

                    byte[] buffer = new byte[1024];

                    int read;
                    while ((read = read(stdin, buffer)) != -1) {
                        channel.writeAndFlush(Unpooled.copiedBuffer(buffer, 0, read));
                    }

                    // we close the writing side of the socket, but keep the read side open to transfer stdout/stderr
                    channel.shutdownOutput();

                }
            }).start();
        }
    }

    public <T> T post(final Object entity, TypeReference<T> typeReference) {

        ResponseCallback<T> callback = new ResponseCallback<T>();

        post(entity, typeReference, callback);

        return callback.awaitResult();
    }

    public <T> void post(final Object entity, TypeReference<T> typeReference, final ResultCallback<T> resultCallback) {

        HttpRequestProvider requestProvider = httpPostRequestProvider(entity);

        Channel channel = getChannel();

        JsonResponseCallbackHandler<T> jsonResponseHandler = new JsonResponseCallbackHandler<T>(typeReference,
                resultCallback);

        HttpResponseHandler responseHandler = new HttpResponseHandler(requestProvider, resultCallback);

        channel.pipeline().addLast(responseHandler);
        channel.pipeline().addLast(new JsonObjectDecoder());
        channel.pipeline().addLast(jsonResponseHandler);

        sendRequest(requestProvider, channel);

        return;
    }

    private HttpRequest prepareDeleteRequest(String uri) {

        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.DELETE, uri);

        setDefaultHeaders(request);

        return request;
    }

    private FullHttpRequest prepareGetRequest(String uri) {

        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri);

        setDefaultHeaders(request);

        return request;
    }

    private HttpRequest preparePostRequest(String uri, Object entity) {
        return prepareEntityRequest(uri, entity, HttpMethod.POST);
    }

    private HttpRequest preparePutRequest(String uri, Object entity) {
        return prepareEntityRequest(uri, entity, HttpMethod.PUT);
    }

    private HttpRequest prepareEntityRequest(String uri, Object entity, HttpMethod httpMethod) {

        HttpRequest request = null;

        if (entity != null) {

            FullHttpRequest fullRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, httpMethod, uri);

            byte[] bytes;
            try {
                bytes = new ObjectMapper().writeValueAsBytes(entity);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            fullRequest.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
            fullRequest.content().clear().writeBytes(Unpooled.copiedBuffer(bytes));
            fullRequest.headers().set(HttpHeaderNames.CONTENT_LENGTH, bytes.length);

            request = fullRequest;
        } else {
            request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, httpMethod, uri);
            request.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);
        }

        setDefaultHeaders(request);

        return request;
    }

    private void sendRequest(HttpRequestProvider requestProvider, Channel channel) {

        ChannelFuture channelFuture = channel.writeAndFlush(requestProvider.getHttpRequest(resource));

        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
            }
        });
    }

    private void setDefaultHeaders(HttpRequest request) {
        request.headers().set(HttpHeaderNames.HOST, "");
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            request.headers().set((CharSequence) entry.getKey(), entry.getValue());
        }
    }

    public <T> T post(TypeReference<T> typeReference, InputStream body) {

        ResponseCallback<T> callback = new ResponseCallback<T>();

        post(typeReference, callback, body);

        return callback.awaitResult();
    }

    public <T> void post(TypeReference<T> typeReference, ResultCallback<T> resultCallback, InputStream body) {
        HttpRequestProvider requestProvider = httpPostRequestProvider(null);

        Channel channel = getChannel();

        JsonResponseCallbackHandler<T> jsonResponseHandler = new JsonResponseCallbackHandler<T>(typeReference,
                resultCallback);

        HttpResponseHandler responseHandler = new HttpResponseHandler(requestProvider, resultCallback);

        channel.pipeline().addLast(new ChunkedWriteHandler());
        channel.pipeline().addLast(responseHandler);
        channel.pipeline().addLast(new JsonObjectDecoder());
        channel.pipeline().addLast(jsonResponseHandler);

        postChunkedStreamRequest(requestProvider, channel, body);
    }

    public void postStream(InputStream body) {
        SkipResultCallback resultCallback = new SkipResultCallback();

        HttpRequestProvider requestProvider = httpPostRequestProvider(null);

        Channel channel = getChannel();

        HttpResponseHandler responseHandler = new HttpResponseHandler(requestProvider, resultCallback);

        channel.pipeline().addLast(new ChunkedWriteHandler());
        channel.pipeline().addLast(responseHandler);

        postChunkedStreamRequest(requestProvider, channel, body);

        try {
            resultCallback.awaitCompletion();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void postChunkedStreamRequest(HttpRequestProvider requestProvider, Channel channel, InputStream body) {
        HttpRequest request = requestProvider.getHttpRequest(resource);

        // don't accept FullHttpRequest here
        if (request instanceof FullHttpRequest) {
            throw new DockerClientException("fatal: request is instance of FullHttpRequest");
        }

        request.headers().set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
        request.headers().remove(HttpHeaderNames.CONTENT_LENGTH);

        channel.write(request);

        channel.write(new ChunkedStream(new BufferedInputStream(body, 1024 * 1024), 1024 * 1024));
        channel.write(LastHttpContent.EMPTY_LAST_CONTENT);
        channel.flush();
    }

    public InputStream get() {
        HttpRequestProvider requestProvider = httpGetRequestProvider();

        Channel channel = getChannel();

        AsyncResultCallback<InputStream> resultCallback = new AsyncResultCallback<>();

        HttpResponseHandler responseHandler = new HttpResponseHandler(requestProvider, resultCallback);

        HttpResponseStreamHandler streamHandler = new HttpResponseStreamHandler(resultCallback);

        channel.pipeline().addLast(responseHandler);
        channel.pipeline().addLast(streamHandler);

        sendRequest(requestProvider, channel);

        return resultCallback.awaitResult();
    }

    public void put(InputStream body, MediaType mediaType) {
        HttpRequestProvider requestProvider = httpPutRequestProvider(null);

        Channel channel = getChannel();

        ResponseCallback<Void> resultCallback = new ResponseCallback<Void>();

        HttpResponseHandler responseHandler = new HttpResponseHandler(requestProvider, resultCallback);

        channel.pipeline().addLast(new ChunkedWriteHandler());
        channel.pipeline().addLast(responseHandler);

        HttpRequest request = requestProvider.getHttpRequest(resource);

        // don't accept FullHttpRequest here
        if (request instanceof FullHttpRequest) {
            throw new DockerClientException("fatal: request is instance of FullHttpRequest");
        }

        request.headers().set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
        request.headers().remove(HttpHeaderNames.CONTENT_LENGTH);
        request.headers().set(HttpHeaderNames.CONTENT_TYPE, mediaType.getMediaType());

        channel.write(request);
        channel.write(new ChunkedStream(new BufferedInputStream(body, 1024 * 1024)));
        channel.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

        resultCallback.awaitResult();
    };
}
