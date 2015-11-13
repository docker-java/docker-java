package com.github.dockerjava.netty.handler;

import java.nio.charset.Charset;

import com.github.dockerjava.api.BadRequestException;
import com.github.dockerjava.api.ConflictException;
import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.InternalServerErrorException;
import com.github.dockerjava.api.NotAcceptableException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.NotModifiedException;
import com.github.dockerjava.api.UnauthorizedException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;

public class HttpResponseInboundHandler extends SimpleChannelInboundHandler<HttpObject> {

	private HttpRequestProvider requestProvider;

	private HttpResponse response;

	private ByteBuf body = Unpooled.buffer();

	public HttpResponseInboundHandler(HttpRequestProvider requestProvider) {
		super(false);
		this.requestProvider = requestProvider;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
		if (msg instanceof HttpResponse) {
			HttpResponse response = (HttpResponse) msg;

			System.err.println("STATUS: " + response.status());
			System.err.println("VERSION: " + response.protocolVersion());
			System.err.println();

			this.response = response;

			if (!response.headers().isEmpty()) {
				for (CharSequence name : response.headers().names()) {
					for (CharSequence value : response.headers().getAll(name)) {
						System.err.println("HEADER: " + name + " = " + value);
					}
				}
				System.err.println();
			}

		} else if (msg instanceof HttpContent) {
			//System.err.println("Got http content");

			HttpContent content = (HttpContent) msg;

			ByteBuf byteBuf = content.content();

			switch (response.status().code()) {
			case 200:
			case 201:
			case 204:
				ctx.fireChannelRead(byteBuf);
				break;
			default:
				body.writeBytes(byteBuf);
			}

			if (content instanceof LastHttpContent) {

				try {
					switch (response.status().code()) {
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

							ctx.channel().writeAndFlush(redirected);
						} else {
							ctx.fireExceptionCaught(new RuntimeException("missing 'location' header for redirect"));
						}
						break;
					case 304:
						throw new NotModifiedException(getBodyAsMessage(body));
					case 400:
						throw new BadRequestException(getBodyAsMessage(body));
					case 401:
						throw new UnauthorizedException(getBodyAsMessage(body));
					case 404:
						throw new NotFoundException(getBodyAsMessage(body));
					case 406:
						throw new NotAcceptableException(getBodyAsMessage(body));
					case 409:
						throw new ConflictException(getBodyAsMessage(body));
					case 500:
						throw new InternalServerErrorException(getBodyAsMessage(body));
					default:
						throw new DockerException(getBodyAsMessage(body), response.status().code());
					}
				} finally {
					 //ctx.close();
				}

			}
		} else {
			System.err.println("UNKNOWN");
		}

	}

	private String getBodyAsMessage(ByteBuf body) {
		return body.readBytes(body.readableBytes()).toString(Charset.forName("UTF-8"));
	}

}
