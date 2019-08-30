package com.github.dockerjava.netty.handler;

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

import java.io.Closeable;
import java.nio.charset.Charset;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.exception.BadRequestException;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.InternalServerErrorException;
import com.github.dockerjava.api.exception.NotAcceptableException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.exception.NotModifiedException;
import com.github.dockerjava.api.exception.UnauthorizedException;

/**
 * Handler that is responsible to handle an incoming {@link HttpResponse}. It evaluates the status code and triggers the appropriate
 * lifecycle methods at the passed {@link ResultCallback}.
 *
 * @author Marcus Linke
 */
public class HttpResponseHandler extends SimpleChannelInboundHandler<HttpObject> {

    private HttpResponse response;

    private ByteBuf errorBody = Unpooled.buffer();

    private HttpRequestProvider requestProvider;

    private ResultCallback<?> resultCallback;

    public HttpResponseHandler(HttpRequestProvider requestProvider, ResultCallback<?> resultCallback) {
        super(false);
        this.requestProvider = requestProvider;
        this.resultCallback = resultCallback;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpResponse) {

            response = (HttpResponse) msg;

            resultCallback.onStart(new Closeable() {
                @Override
                public void close() {
                    ctx.channel().close();
                }
            });

        } else if (msg instanceof HttpContent) {

            HttpContent content = (HttpContent) msg;

            ByteBuf byteBuf = content.content();

            switch (response.status().code()) {
                case 200:
                case 201:
                case 204:
                    ctx.fireChannelRead(byteBuf);
                    break;
                default:
                    errorBody.writeBytes(byteBuf);
            }

            if (content instanceof LastHttpContent) {
                try {

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
                                HttpRequest redirected = requestProvider.getHttpRequest(location);

                                ctx.channel().writeAndFlush(redirected);
                            }
                            break;
                        case 304:
                            throw new NotModifiedException(getBodyAsMessage(errorBody));
                        case 400:
                            throw new BadRequestException(getBodyAsMessage(errorBody));
                        case 401:
                            throw new UnauthorizedException(getBodyAsMessage(errorBody));
                        case 404:
                            throw new NotFoundException(getBodyAsMessage(errorBody));
                        case 406:
                            throw new NotAcceptableException(getBodyAsMessage(errorBody));
                        case 409:
                            throw new ConflictException(getBodyAsMessage(errorBody));
                        case 500:
                            throw new InternalServerErrorException(getBodyAsMessage(errorBody));
                        default:
                            throw new DockerException(getBodyAsMessage(errorBody), response.status().code());
                    }
                } catch (Throwable e) {
                    resultCallback.onError(e);
                } finally {
                    resultCallback.onComplete();
                }
            }
        }
    }

    private String getBodyAsMessage(ByteBuf body) {
        String result = body.readBytes(body.readableBytes()).toString(Charset.forName("UTF-8"));
        body.discardReadBytes();
        body.release();
        return result;
    }
}
