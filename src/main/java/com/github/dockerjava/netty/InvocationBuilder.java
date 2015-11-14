package com.github.dockerjava.netty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.netty.handler.AwaitObjectInboundHandler;
import com.github.dockerjava.netty.handler.DeserializeJsonInboundHandler;
import com.github.dockerjava.netty.handler.DockerRawStreamHandler;
import com.github.dockerjava.netty.handler.HijackHttpConnectionHandler;
import com.github.dockerjava.netty.handler.HttpRequestProvider;
import com.github.dockerjava.netty.handler.HttpResponseInboundHandler;
import com.github.dockerjava.netty.handler.HttpResponseStreamInboundHandler;

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
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.json.JsonObjectDecoder;

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
		FullHttpRequest request = prepareGetRequest(resource);

		Channel channel = channelProvider.getChannel();

		AwaitObjectInboundHandler<T> awaitObject = new AwaitObjectInboundHandler<T>(resultType);

		channel.pipeline().addLast(new HttpResponseInboundHandler(new HttpRequestProvider() {
			@Override
			public HttpRequest getHttpRequest(String uri) {
				return prepareGetRequest(uri);
			}
		}));
		channel.pipeline().addLast(new JsonObjectDecoder());
		channel.pipeline().addLast(new DeserializeJsonInboundHandler<T>(resultType));
		channel.pipeline().addLast(awaitObject);

		System.out.println("resource: " + resource);

		channel.writeAndFlush(request);

		// Wait for the server to close the connection.
		try {
			// channel.closeFuture().sync();
			T response = awaitObject.await();

			// channel.disconnect();

			return response;
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private FullHttpRequest prepareGetRequest(String uri) {
		// Prepare the HTTP request.
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

		HttpRequest request = preparePostRequest(resource, entity);

		AwaitObjectInboundHandler<T> awaitObject = new AwaitObjectInboundHandler<T>(resultType);

		Channel channel = channelProvider.getChannel();

		channel.pipeline().addLast(new HttpResponseInboundHandler(new HttpRequestProvider() {
			@Override
			public HttpRequest getHttpRequest(String uri) {
				return preparePostRequest(uri, entity);
			}
		}));
		channel.pipeline().addLast(new JsonObjectDecoder());
		channel.pipeline().addLast(new DeserializeJsonInboundHandler<T>(resultType));
		channel.pipeline().addLast(awaitObject);

		System.out.println("resource: " + resource);
		System.out.println(channel.isActive());

		channel.writeAndFlush(request);

		// Wait for the server to close the connection.
		try {
			// channel.closeFuture().sync();
			T response = awaitObject.await();

			return response;
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

	}

	public InputStream post(final Object entity, final InputStream stdin) {

		HttpRequest request = preparePostRequest(resource, entity);

		HttpResponseStreamInboundHandler streamHandler = new HttpResponseStreamInboundHandler();

		final Channel channel = channelProvider.getChannel();

		HijackHttpConnectionHandler hijackHandler = new HijackHttpConnectionHandler();
		channel.pipeline()
				.addLast(new HttpClientUpgradeHandler(new HttpClientCodec(), hijackHandler, Integer.MAX_VALUE));
		channel.pipeline().addLast(new HttpResponseInboundHandler(new HttpRequestProvider() {
			@Override
			public HttpRequest getHttpRequest(String uri) {
				return preparePostRequest(uri, entity);
			}
		}));

		channel.pipeline().addLast(streamHandler);

		channel.writeAndFlush(request);

		// wait for successful http upgrade procedure
		hijackHandler.await();

		// start a new thread that reads from stdin and writes to the channel
		new Thread(new Runnable() {

			@Override
			public void run() {

				BufferedReader reader = new BufferedReader(new InputStreamReader(stdin));

				int read = -1;
				while ((read = read(reader)) != -1) {
					byte[] bytes = ByteBuffer.allocate(4).putInt(read).array();
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

		return streamHandler.getInputStream();
	}

	public void post(final Object entity, final OutputStream stdout, final OutputStream stderr,
			final InputStream stdin) {

		HttpRequest request = preparePostRequest(resource, entity);

		DockerRawStreamHandler streamHandler = new DockerRawStreamHandler(stdout, stderr);

		final Channel channel = channelProvider.getChannel();

		HijackHttpConnectionHandler hijackHandler = new HijackHttpConnectionHandler();
		channel.pipeline()
				.addLast(new HttpClientUpgradeHandler(new HttpClientCodec(), hijackHandler, Integer.MAX_VALUE));
		channel.pipeline().addLast(new HttpResponseInboundHandler(new HttpRequestProvider() {
			@Override
			public HttpRequest getHttpRequest(String uri) {
				return preparePostRequest(uri, entity);
			}
		}));

		channel.pipeline().addLast(streamHandler);

		channel.writeAndFlush(request);

		// wait for successful http upgrade procedure
		hijackHandler.await();

		// start a new thread that reads from stdin and writes to the channel
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

	private HttpRequest preparePostRequest(String uri, Object entity) {
		// Prepare the HTTP request.
		FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, uri);

		setDefaultHeaders(request);

		byte[] bytes;
		try {
			bytes = new ObjectMapper().writeValueAsBytes(entity);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		request.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
		request.content().clear().writeBytes(Unpooled.copiedBuffer(bytes));
		request.headers().set(HttpHeaderNames.CONTENT_LENGTH, bytes.length);

		return request;
	}

}
