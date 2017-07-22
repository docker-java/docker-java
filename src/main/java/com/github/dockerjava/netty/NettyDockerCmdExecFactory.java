package com.github.dockerjava.netty;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.Security;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;

import org.apache.commons.lang.SystemUtils;

import com.github.dockerjava.api.command.AttachContainerCmd;
import com.github.dockerjava.api.command.AuthCmd;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.command.CommitCmd;
import com.github.dockerjava.api.command.ConnectToNetworkCmd;
import com.github.dockerjava.api.command.ContainerDiffCmd;
import com.github.dockerjava.api.command.CopyArchiveFromContainerCmd;
import com.github.dockerjava.api.command.CopyArchiveToContainerCmd;
import com.github.dockerjava.api.command.CopyFileFromContainerCmd;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateImageCmd;
import com.github.dockerjava.api.command.CreateNetworkCmd;
import com.github.dockerjava.api.command.CreateServiceCmd;
import com.github.dockerjava.api.command.CreateVolumeCmd;
import com.github.dockerjava.api.command.DisconnectFromNetworkCmd;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.api.command.EventsCmd;
import com.github.dockerjava.api.command.ExecCreateCmd;
import com.github.dockerjava.api.command.ExecStartCmd;
import com.github.dockerjava.api.command.InfoCmd;
import com.github.dockerjava.api.command.InitializeSwarmCmd;
import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectExecCmd;
import com.github.dockerjava.api.command.InspectImageCmd;
import com.github.dockerjava.api.command.InspectNetworkCmd;
import com.github.dockerjava.api.command.InspectServiceCmd;
import com.github.dockerjava.api.command.InspectSwarmCmd;
import com.github.dockerjava.api.command.InspectSwarmNodeCmd;
import com.github.dockerjava.api.command.InspectVolumeCmd;
import com.github.dockerjava.api.command.JoinSwarmCmd;
import com.github.dockerjava.api.command.KillContainerCmd;
import com.github.dockerjava.api.command.LeaveSwarmCmd;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.command.ListNetworksCmd;
import com.github.dockerjava.api.command.ListServicesCmd;
import com.github.dockerjava.api.command.ListSwarmNodesCmd;
import com.github.dockerjava.api.command.ListVolumesCmd;
import com.github.dockerjava.api.command.LoadImageCmd;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.command.PauseContainerCmd;
import com.github.dockerjava.api.command.PingCmd;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.PushImageCmd;
import com.github.dockerjava.api.command.RemoveContainerCmd;
import com.github.dockerjava.api.command.RemoveImageCmd;
import com.github.dockerjava.api.command.RemoveNetworkCmd;
import com.github.dockerjava.api.command.RemoveServiceCmd;
import com.github.dockerjava.api.command.RemoveSwarmNodeCmd;
import com.github.dockerjava.api.command.RemoveVolumeCmd;
import com.github.dockerjava.api.command.RenameContainerCmd;
import com.github.dockerjava.api.command.RestartContainerCmd;
import com.github.dockerjava.api.command.SaveImageCmd;
import com.github.dockerjava.api.command.SearchImagesCmd;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.command.StatsCmd;
import com.github.dockerjava.api.command.StopContainerCmd;
import com.github.dockerjava.api.command.TagImageCmd;
import com.github.dockerjava.api.command.TopContainerCmd;
import com.github.dockerjava.api.command.UnpauseContainerCmd;
import com.github.dockerjava.api.command.UpdateContainerCmd;
import com.github.dockerjava.api.command.UpdateServiceCmd;
import com.github.dockerjava.api.command.UpdateSwarmCmd;
import com.github.dockerjava.api.command.UpdateSwarmNodeCmd;
import com.github.dockerjava.api.command.VersionCmd;
import com.github.dockerjava.api.command.WaitContainerCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.SSLConfig;
import com.github.dockerjava.netty.exec.AttachContainerCmdExec;
import com.github.dockerjava.netty.exec.AuthCmdExec;
import com.github.dockerjava.netty.exec.BuildImageCmdExec;
import com.github.dockerjava.netty.exec.CommitCmdExec;
import com.github.dockerjava.netty.exec.ConnectToNetworkCmdExec;
import com.github.dockerjava.netty.exec.ContainerDiffCmdExec;
import com.github.dockerjava.netty.exec.CopyArchiveFromContainerCmdExec;
import com.github.dockerjava.netty.exec.CopyArchiveToContainerCmdExec;
import com.github.dockerjava.netty.exec.CopyFileFromContainerCmdExec;
import com.github.dockerjava.netty.exec.CreateContainerCmdExec;
import com.github.dockerjava.netty.exec.CreateImageCmdExec;
import com.github.dockerjava.netty.exec.CreateNetworkCmdExec;
import com.github.dockerjava.netty.exec.CreateServiceCmdExec;
import com.github.dockerjava.netty.exec.CreateVolumeCmdExec;
import com.github.dockerjava.netty.exec.DisconnectFromNetworkCmdExec;
import com.github.dockerjava.netty.exec.EventsCmdExec;
import com.github.dockerjava.netty.exec.ExecCreateCmdExec;
import com.github.dockerjava.netty.exec.ExecStartCmdExec;
import com.github.dockerjava.netty.exec.InfoCmdExec;
import com.github.dockerjava.netty.exec.InitializeSwarmCmdExec;
import com.github.dockerjava.netty.exec.InspectContainerCmdExec;
import com.github.dockerjava.netty.exec.InspectExecCmdExec;
import com.github.dockerjava.netty.exec.InspectImageCmdExec;
import com.github.dockerjava.netty.exec.InspectNetworkCmdExec;
import com.github.dockerjava.netty.exec.InspectServiceCmdExec;
import com.github.dockerjava.netty.exec.InspectSwarmCmdExec;
import com.github.dockerjava.netty.exec.InspectSwarmNodeCmdExec;
import com.github.dockerjava.netty.exec.InspectVolumeCmdExec;
import com.github.dockerjava.netty.exec.JoinSwarmCmdExec;
import com.github.dockerjava.netty.exec.KillContainerCmdExec;
import com.github.dockerjava.netty.exec.LeaveSwarmCmdExec;
import com.github.dockerjava.netty.exec.ListContainersCmdExec;
import com.github.dockerjava.netty.exec.ListImagesCmdExec;
import com.github.dockerjava.netty.exec.ListNetworksCmdExec;
import com.github.dockerjava.netty.exec.ListServicesCmdExec;
import com.github.dockerjava.netty.exec.ListSwarmNodesCmdExec;
import com.github.dockerjava.netty.exec.ListVolumesCmdExec;
import com.github.dockerjava.netty.exec.LoadImageCmdExec;
import com.github.dockerjava.netty.exec.LogContainerCmdExec;
import com.github.dockerjava.netty.exec.PauseContainerCmdExec;
import com.github.dockerjava.netty.exec.PingCmdExec;
import com.github.dockerjava.netty.exec.PullImageCmdExec;
import com.github.dockerjava.netty.exec.PushImageCmdExec;
import com.github.dockerjava.netty.exec.RemoveContainerCmdExec;
import com.github.dockerjava.netty.exec.RemoveImageCmdExec;
import com.github.dockerjava.netty.exec.RemoveNetworkCmdExec;
import com.github.dockerjava.netty.exec.RemoveServiceCmdExec;
import com.github.dockerjava.netty.exec.RemoveSwarmNodeCmdExec;
import com.github.dockerjava.netty.exec.RemoveVolumeCmdExec;
import com.github.dockerjava.netty.exec.RenameContainerCmdExec;
import com.github.dockerjava.netty.exec.RestartContainerCmdExec;
import com.github.dockerjava.netty.exec.SaveImageCmdExec;
import com.github.dockerjava.netty.exec.SearchImagesCmdExec;
import com.github.dockerjava.netty.exec.StartContainerCmdExec;
import com.github.dockerjava.netty.exec.StatsCmdExec;
import com.github.dockerjava.netty.exec.StopContainerCmdExec;
import com.github.dockerjava.netty.exec.TagImageCmdExec;
import com.github.dockerjava.netty.exec.TopContainerCmdExec;
import com.github.dockerjava.netty.exec.UnpauseContainerCmdExec;
import com.github.dockerjava.netty.exec.UpdateContainerCmdExec;
import com.github.dockerjava.netty.exec.UpdateServiceCmdExec;
import com.github.dockerjava.netty.exec.UpdateSwarmCmdExec;
import com.github.dockerjava.netty.exec.UpdateSwarmNodeCmdExec;
import com.github.dockerjava.netty.exec.VersionCmdExec;
import com.github.dockerjava.netty.exec.WaitContainerCmdExec;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFactory;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollDomainSocketChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.kqueue.KQueueDomainSocketChannel;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DuplexChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.unix.DomainSocketAddress;
import io.netty.channel.unix.UnixChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


/**
 * Experimental implementation of {@link DockerCmdExecFactory} that supports http connection hijacking that is needed to pass STDIN to the
 * container.
 * <p>
 * To use it just pass an instance via {@link DockerClientImpl#withDockerCmdExecFactory(DockerCmdExecFactory)}
 *
 * @author Marcus Linke
 * @see https://docs.docker.com/engine/reference/api/docker_remote_api_v1.21/#attach-to-a-container
 * @see https://docs.docker.com/engine/reference/api/docker_remote_api_v1.21/#exec-start
 */
public class NettyDockerCmdExecFactory implements DockerCmdExecFactory {

    private static String threadPrefix = "dockerjava-netty";

    /*
     * useful links:
     *
     * http://stackoverflow.com/questions/33296749/netty-connect-to-unix-domain-socket-failed
     * http://netty.io/wiki/native-transports.html
     * https://github.com/netty/netty/blob/master/example/src/main/java/io/netty/example/http/snoop/HttpSnoopClient.java
     * https://github.com/slandelle/netty-request-chunking/blob/master/src/test/java/slandelle/ChunkingTest.java
     */

    private DockerClientConfig dockerClientConfig;

    private Bootstrap bootstrap;

    private EventLoopGroup eventLoopGroup;

    private NettyInitializer nettyInitializer;

    private WebTarget baseResource;

    private ChannelProvider channelProvider = new ChannelProvider() {
        @Override
        public DuplexChannel getChannel() {
            DuplexChannel channel = connect();
            channel.pipeline().addLast(new LoggingHandler(getClass()));
            return channel;
        }
    };

    private Integer connectTimeout = null;

    @Override
    public void init(DockerClientConfig dockerClientConfig) {
        checkNotNull(dockerClientConfig, "config was not specified");
        this.dockerClientConfig = dockerClientConfig;

        bootstrap = new Bootstrap();

        String scheme = dockerClientConfig.getDockerHost().getScheme();

        if ("unix".equals(scheme)) {
            nettyInitializer = new UnixDomainSocketInitializer();
        } else if ("tcp".equals(scheme)) {
            nettyInitializer = new InetSocketInitializer();
        }

        eventLoopGroup = nettyInitializer.init(bootstrap, dockerClientConfig);

        baseResource = new WebTarget(channelProvider).path(dockerClientConfig.getApiVersion().asWebPathPart());
    }

    private DuplexChannel connect() {
        try {
            return connect(bootstrap);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private DuplexChannel connect(final Bootstrap bootstrap) throws InterruptedException {
        return nettyInitializer.connect(bootstrap);
    }

    private interface NettyInitializer {
        EventLoopGroup init(final Bootstrap bootstrap, DockerClientConfig dockerClientConfig);

        DuplexChannel connect(final Bootstrap bootstrap) throws InterruptedException;
    }

    private class UnixDomainSocketInitializer implements NettyInitializer {
        @Override
        public EventLoopGroup init(Bootstrap bootstrap, DockerClientConfig dockerClientConfig) {
            if (SystemUtils.IS_OS_LINUX) {
                return epollGroup();
            } else if (SystemUtils.IS_OS_MAC_OSX) {
                return kqueueGroup();
            }
            throw new RuntimeException("Unspported OS");
        }

        public EventLoopGroup epollGroup() {
            EventLoopGroup epollEventLoopGroup = new EpollEventLoopGroup(0, new DefaultThreadFactory(threadPrefix));

            ChannelFactory<EpollDomainSocketChannel> factory = new ChannelFactory<EpollDomainSocketChannel>() {
                @Override
                public EpollDomainSocketChannel newChannel() {
                    return configure(new EpollDomainSocketChannel());
                }
            };

            bootstrap.group(epollEventLoopGroup).channelFactory(factory).handler(new ChannelInitializer<UnixChannel>() {
                @Override
                protected void initChannel(final UnixChannel channel) throws Exception {
                    channel.pipeline().addLast(new HttpClientCodec());
                }
            });
            return epollEventLoopGroup;
        }

        public EventLoopGroup kqueueGroup() {
            EventLoopGroup nioEventLoopGroup = new KQueueEventLoopGroup(0, new DefaultThreadFactory(threadPrefix));

            bootstrap.group(nioEventLoopGroup).channel(KQueueDomainSocketChannel.class)
                    .handler(new ChannelInitializer<KQueueDomainSocketChannel>() {
                        @Override
                        protected void initChannel(final KQueueDomainSocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new LoggingHandler(getClass()));
                            channel.pipeline().addLast(new HttpClientCodec());
                        }
                    });

            return nioEventLoopGroup;
        }

        @Override
        public DuplexChannel connect(Bootstrap bootstrap) throws InterruptedException {
            return (DuplexChannel) bootstrap.connect(new DomainSocketAddress("/var/run/docker.sock")).sync().channel();
        }
    }

    private class InetSocketInitializer implements NettyInitializer {
        @Override
        public EventLoopGroup init(Bootstrap bootstrap, final DockerClientConfig dockerClientConfig) {
            EventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(0, new DefaultThreadFactory(threadPrefix));

            InetAddress addr = InetAddress.getLoopbackAddress();

            final SocketAddress proxyAddress = new InetSocketAddress(addr, 8008);

            Security.addProvider(new BouncyCastleProvider());

            ChannelFactory<NioSocketChannel> factory = new ChannelFactory<NioSocketChannel>() {
                @Override
                public NioSocketChannel newChannel() {
                    return configure(new NioSocketChannel());
                }
            };

            bootstrap.group(nioEventLoopGroup).channelFactory(factory)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(final SocketChannel channel) throws Exception {
                            // channel.pipeline().addLast(new
                            // HttpProxyHandler(proxyAddress));
                            channel.pipeline().addLast(new HttpClientCodec());
                        }
                    });

            return nioEventLoopGroup;
        }

        @Override
        public DuplexChannel connect(Bootstrap bootstrap) throws InterruptedException {
            String host = dockerClientConfig.getDockerHost().getHost();
            int port = dockerClientConfig.getDockerHost().getPort();

            if (port == -1) {
                throw new RuntimeException("no port configured for " + host);
            }

            DuplexChannel channel = (DuplexChannel) bootstrap.connect(host, port).sync().channel();

            final SslHandler ssl = initSsl(dockerClientConfig);

            if (ssl != null) {
                channel.pipeline().addFirst(ssl);
            }

            return channel;
        }

        private SslHandler initSsl(DockerClientConfig dockerClientConfig) {
            SslHandler ssl = null;

            try {
                String host = dockerClientConfig.getDockerHost().getHost();
                int port = dockerClientConfig.getDockerHost().getPort();

                final SSLConfig sslConfig = dockerClientConfig.getSSLConfig();

                if (sslConfig != null && sslConfig.getSSLContext() != null) {

                    SSLEngine engine = sslConfig.getSSLContext().createSSLEngine(host, port);
                    engine.setUseClientMode(true);
                    engine.setSSLParameters(enableHostNameVerification(engine.getSSLParameters()));

                    // in the future we may use HostnameVerifier like here:
                    // https://github.com/AsyncHttpClient/async-http-client/blob/1.8.x/src/main/java/com/ning/http/client/providers/netty/NettyConnectListener.java#L76

                    ssl = new SslHandler(engine);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return ssl;
        }
    }

    protected DockerClientConfig getDockerClientConfig() {
        checkNotNull(dockerClientConfig,
                "Factor not initialized, dockerClientConfig not set. You probably forgot to call init()!");
        return dockerClientConfig;
    }

    public SSLParameters enableHostNameVerification(SSLParameters sslParameters) {
        sslParameters.setEndpointIdentificationAlgorithm("HTTPS");
        return sslParameters;
    }

    @Override
    public CopyArchiveFromContainerCmd.Exec createCopyArchiveFromContainerCmdExec() {
        return new CopyArchiveFromContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public CopyArchiveToContainerCmd.Exec createCopyArchiveToContainerCmdExec() {
        return new CopyArchiveToContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public AuthCmd.Exec createAuthCmdExec() {
        return new AuthCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public InfoCmd.Exec createInfoCmdExec() {
        return new InfoCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public PingCmd.Exec createPingCmdExec() {
        return new PingCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public VersionCmd.Exec createVersionCmdExec() {
        return new VersionCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public PullImageCmd.Exec createPullImageCmdExec() {
        return new PullImageCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public PushImageCmd.Exec createPushImageCmdExec() {
        return new PushImageCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public SaveImageCmd.Exec createSaveImageCmdExec() {
        return new SaveImageCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public CreateImageCmd.Exec createCreateImageCmdExec() {
        return new CreateImageCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public LoadImageCmd.Exec createLoadImageCmdExec() {
        return new LoadImageCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public SearchImagesCmd.Exec createSearchImagesCmdExec() {
        return new SearchImagesCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public RemoveImageCmd.Exec createRemoveImageCmdExec() {
        return new RemoveImageCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public ListImagesCmd.Exec createListImagesCmdExec() {
        return new ListImagesCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public InspectImageCmd.Exec createInspectImageCmdExec() {
        return new InspectImageCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public ListContainersCmd.Exec createListContainersCmdExec() {
        return new ListContainersCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public CreateContainerCmd.Exec createCreateContainerCmdExec() {
        return new CreateContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public StartContainerCmd.Exec createStartContainerCmdExec() {
        return new StartContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public InspectContainerCmd.Exec createInspectContainerCmdExec() {
        return new InspectContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public ExecCreateCmd.Exec createExecCmdExec() {
        return new ExecCreateCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public RemoveContainerCmd.Exec createRemoveContainerCmdExec() {
        return new RemoveContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public WaitContainerCmd.Exec createWaitContainerCmdExec() {
        return new WaitContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public AttachContainerCmd.Exec createAttachContainerCmdExec() {
        return new AttachContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public ExecStartCmd.Exec createExecStartCmdExec() {
        return new ExecStartCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public InspectExecCmd.Exec createInspectExecCmdExec() {
        return new InspectExecCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public LogContainerCmd.Exec createLogContainerCmdExec() {
        return new LogContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public CopyFileFromContainerCmd.Exec createCopyFileFromContainerCmdExec() {
        return new CopyFileFromContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public StopContainerCmd.Exec createStopContainerCmdExec() {
        return new StopContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public ContainerDiffCmd.Exec createContainerDiffCmdExec() {
        return new ContainerDiffCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public KillContainerCmd.Exec createKillContainerCmdExec() {
        return new KillContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public UpdateContainerCmd.Exec createUpdateContainerCmdExec() {
        return new UpdateContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public RenameContainerCmd.Exec createRenameContainerCmdExec() {
        return new RenameContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public RestartContainerCmd.Exec createRestartContainerCmdExec() {
        return new RestartContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public CommitCmd.Exec createCommitCmdExec() {
        return new CommitCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public BuildImageCmd.Exec createBuildImageCmdExec() {
        return new BuildImageCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public TopContainerCmd.Exec createTopContainerCmdExec() {
        return new TopContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public TagImageCmd.Exec createTagImageCmdExec() {
        return new TagImageCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public PauseContainerCmd.Exec createPauseContainerCmdExec() {
        return new PauseContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public UnpauseContainerCmd.Exec createUnpauseContainerCmdExec() {
        return new UnpauseContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public EventsCmd.Exec createEventsCmdExec() {
        return new EventsCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public StatsCmd.Exec createStatsCmdExec() {
        return new StatsCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public CreateVolumeCmd.Exec createCreateVolumeCmdExec() {
        return new CreateVolumeCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public InspectVolumeCmd.Exec createInspectVolumeCmdExec() {
        return new InspectVolumeCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public RemoveVolumeCmd.Exec createRemoveVolumeCmdExec() {
        return new RemoveVolumeCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public ListVolumesCmd.Exec createListVolumesCmdExec() {
        return new ListVolumesCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public ListNetworksCmd.Exec createListNetworksCmdExec() {
        return new ListNetworksCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public InspectNetworkCmd.Exec createInspectNetworkCmdExec() {
        return new InspectNetworkCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public CreateNetworkCmd.Exec createCreateNetworkCmdExec() {
        return new CreateNetworkCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public RemoveNetworkCmd.Exec createRemoveNetworkCmdExec() {
        return new RemoveNetworkCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public ConnectToNetworkCmd.Exec createConnectToNetworkCmdExec() {
        return new ConnectToNetworkCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public DisconnectFromNetworkCmd.Exec createDisconnectFromNetworkCmdExec() {
        return new DisconnectFromNetworkCmdExec(getBaseResource(), getDockerClientConfig());
    }

    // swarm
    @Override
    public InitializeSwarmCmd.Exec createInitializeSwarmCmdExec() {
        return new InitializeSwarmCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public InspectSwarmCmd.Exec createInspectSwarmCmdExec() {
        return new InspectSwarmCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public JoinSwarmCmd.Exec createJoinSwarmCmdExec() {
        return new JoinSwarmCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public LeaveSwarmCmd.Exec createLeaveSwarmCmdExec() {
        return new LeaveSwarmCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public UpdateSwarmCmd.Exec createUpdateSwarmCmdExec() {
        return new UpdateSwarmCmdExec(getBaseResource(), getDockerClientConfig());
    }

    // services
    @Override
    public ListServicesCmd.Exec createListServicesCmdExec() {
        return new ListServicesCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public CreateServiceCmd.Exec createCreateServiceCmdExec() {
        return new CreateServiceCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public InspectServiceCmd.Exec createInspectServiceCmdExec() {
        return new InspectServiceCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public UpdateServiceCmd.Exec createUpdateServiceCmdExec() {
        return new UpdateServiceCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public RemoveServiceCmd.Exec createRemoveServiceCmdExec() {
        return new RemoveServiceCmdExec(getBaseResource(), getDockerClientConfig());
    }

    // nodes
    @Override
    public ListSwarmNodesCmd.Exec listSwarmNodeCmdExec() {
        return new ListSwarmNodesCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public InspectSwarmNodeCmd.Exec inspectSwarmNodeCmdExec() {
        return new InspectSwarmNodeCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public RemoveSwarmNodeCmd.Exec removeSwarmNodeCmdExec() {
        return new RemoveSwarmNodeCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public UpdateSwarmNodeCmd.Exec updateSwarmNodeCmdExec() {
        return new UpdateSwarmNodeCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public void close() throws IOException {
        checkNotNull(eventLoopGroup, "Factory not initialized. You probably forgot to call init()!");

        eventLoopGroup.shutdownGracefully();
    }

    /**
     * Configure connection timeout in milliseconds
     */
    public NettyDockerCmdExecFactory withConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    private <T extends Channel> T configure(T channel) {
        ChannelConfig channelConfig = channel.config();

        if (connectTimeout != null) {
            channelConfig.setConnectTimeoutMillis(connectTimeout);
        }

        return channel;
    }

    private WebTarget getBaseResource() {
        checkNotNull(baseResource, "Factory not initialized, baseResource not set. You probably forgot to call init()!");
        return baseResource;
    }
}
