package com.github.dockerjava.netty;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static org.testng.Assert.assertEquals;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DefaultDockerClientConfig.Builder;
import com.github.dockerjava.core.DockerClientBuilder;

public class NettyDockerCmdExecFactoryConfigTest {

    @Test
    public void testNettyDockerCmdExecFactoryConfigWithApiVersion() throws Exception {
        int dockerPort = getFreePort();

        NettyDockerCmdExecFactory factory = new NettyDockerCmdExecFactory();
        Builder configBuilder = DefaultDockerClientConfig.createDefaultConfigBuilder()
            .withDockerHost("tcp://localhost:" + dockerPort)
            .withApiVersion("1.23");

        DockerClient client = DockerClientBuilder.getInstance(configBuilder)
                .withDockerCmdExecFactory(factory)
                .build();

        FakeDockerServer server = new FakeDockerServer(dockerPort);
        server.start();
        try {
            client.versionCmd().exec();

            List<HttpRequest> requests = server.getRequests();

            assertEquals(requests.size(), 1);
            assertEquals(requests.get(0).uri(), "/v1.23/version");
        } finally {
            server.stop();
        }
    }

    @Test
    public void testNettyDockerCmdExecFactoryConfigWithoutApiVersion() throws Exception {
        int dockerPort = getFreePort();

        NettyDockerCmdExecFactory factory = new NettyDockerCmdExecFactory();
        Builder configBuilder = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost("tcp://localhost:" + dockerPort);

        DockerClient client = DockerClientBuilder.getInstance(configBuilder)
            .withDockerCmdExecFactory(factory)
            .build();

        FakeDockerServer server = new FakeDockerServer(dockerPort);
        server.start();
        try {
            client.versionCmd().exec();

            List<HttpRequest> requests = server.getRequests();

            assertEquals(requests.size(), 1);
            assertEquals(requests.get(0).uri(), "/version");
        } finally {
            server.stop();
        }
    }

    private int getFreePort() throws IOException {
        ServerSocket socket = new ServerSocket(0);
        int freePort = socket.getLocalPort();
        socket.close();
        return freePort;
    }

    private class FakeDockerServer {
        private final int port;
        private final NioEventLoopGroup parent;
        private final NioEventLoopGroup child;
        private final List<HttpRequest> requests = new ArrayList<>();
        private Channel channel;

        private FakeDockerServer(int port) {
            this.port = port;
            this.parent = new NioEventLoopGroup();
            this.child = new NioEventLoopGroup();
        }

        private void start() throws Exception {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parent, child)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast("codec", new HttpServerCodec());
                        pipeline.addLast("httpHandler", new SimpleChannelInboundHandler<Object>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext context, Object message) throws Exception {
                                if (message instanceof HttpRequest) {
                                    // Keep track of processed requests
                                    HttpRequest request = (HttpRequest) message;
                                    requests.add(request);
                                }

                                if (message instanceof HttpContent) {
                                    // Write an empty JSON response back to the client
                                    FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.copiedBuffer("{}", CharsetUtil.UTF_8));
                                    response.headers().set(CONTENT_TYPE, "application/json; charset=UTF-8");
                                    response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
                                    context.writeAndFlush(response);
                                }
                            }
                        });
                    }
                });

            channel = bootstrap.bind(port).syncUninterruptibly().channel();
        }

        private void stop() throws Exception {
            parent.shutdownGracefully();
            child.shutdownGracefully();
            channel.closeFuture().sync();
        }

        private List<HttpRequest> getRequests() {
            return requests;
        }
    }
}
