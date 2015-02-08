package com.github.dockerjava.jaxrs;

import static jersey.repackaged.com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.net.URI;

import com.github.dockerjava.api.command.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.github.dockerjava.api.DockerClientException;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.util.JsonClientFilter;
import com.github.dockerjava.core.util.ResponseStatusExceptionFilter;
import com.github.dockerjava.core.util.SelectiveLoggingFilter;

public class DockerCmdExecFactoryImpl implements DockerCmdExecFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerCmdExecFactoryImpl.class.getName());
    private Client client;
    private WebTarget baseResource;

    @Override
    public void init(DockerClientConfig dockerClientConfig) {
        checkNotNull(dockerClientConfig, "config was not specified");

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.connectorProvider(new ApacheConnectorProvider());
        clientConfig.property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);

        clientConfig.register(ResponseStatusExceptionFilter.class);
        clientConfig.register(JsonClientFilter.class);
        clientConfig.register(JacksonJsonProvider.class);

        if (dockerClientConfig.isLoggingFilterEnabled()) {
            clientConfig.register(new SelectiveLoggingFilter(LOGGER, true));
        }

        if (dockerClientConfig.getReadTimeout() != null) {
            int readTimeout = dockerClientConfig.getReadTimeout();
            clientConfig.property(ClientProperties.READ_TIMEOUT, readTimeout);
        }

        URI originalUri = dockerClientConfig.getUri();

        SSLContext sslContext;
        try {
            sslContext = dockerClientConfig.getSslConfig().getSSLContext();
        } catch(Exception ex) {
            throw new DockerClientException("Error in SSL Configuration", ex);
        }

        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(getSchemeRegistry(originalUri, sslContext));
        connManager.setMaxTotal(dockerClientConfig.getMaxTotalConnections());
        connManager.setDefaultMaxPerRoute(dockerClientConfig.getMaxPerRoutConnections());
        clientConfig.property(ApacheClientProperties.CONNECTION_MANAGER, connManager);

        ClientBuilder clientBuilder = ClientBuilder.newBuilder().withConfig(clientConfig);

        if (sslContext != null) {
            clientBuilder.sslContext(sslContext);
        }

        client = clientBuilder.build();

        if (originalUri.getScheme().equals("unix")) {
            dockerClientConfig.setUri(UnixConnectionSocketFactory.sanitizeUri(originalUri));
        }
        WebTarget webResource = client.target(dockerClientConfig.getUri());

        if (dockerClientConfig.getVersion() == null || dockerClientConfig.getVersion().isEmpty()) {
            baseResource = webResource;
        } else {
            baseResource = webResource.path("v" + dockerClientConfig.getVersion());
        }
    }

    private org.apache.http.config.Registry<ConnectionSocketFactory> getSchemeRegistry(final URI originalUri, SSLContext sslContext) {
      RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
      registryBuilder.register("http", PlainConnectionSocketFactory.getSocketFactory());
      if (sslContext != null) {
        registryBuilder.register("https", new SSLConnectionSocketFactory(sslContext));
      }
      registryBuilder.register("unix", new UnixConnectionSocketFactory(originalUri));
      return registryBuilder.build();
      }

    protected WebTarget getBaseResource() {
        checkNotNull(baseResource, "Factory not initialized. You probably forgot to call init()!");
        return baseResource;
    }

    @Override
    public AuthCmd.Exec createAuthCmdExec() {
        return new AuthCmdExec(getBaseResource());
    }

    @Override
    public InfoCmd.Exec createInfoCmdExec() {
        return new InfoCmdExec(getBaseResource());
    }

    @Override
    public PingCmd.Exec createPingCmdExec() {
        return new PingCmdExec(getBaseResource());
    }

    @Override
    public VersionCmd.Exec createVersionCmdExec() {
        return new VersionCmdExec(getBaseResource());
    }

    @Override
    public PullImageCmd.Exec createPullImageCmdExec() {
        return new PullImageCmdExec(getBaseResource());
    }

    @Override
    public PushImageCmd.Exec createPushImageCmdExec() {
        return new PushImageCmdExec(getBaseResource());
    }
    
    @Override
    public SaveImageCmd.Exec createSaveImageCmdExec() { return new SaveImageCmdExec(getBaseResource()); }

    @Override
    public CreateImageCmd.Exec createCreateImageCmdExec() {
        return new CreateImageCmdExec(getBaseResource());
    }

    @Override
    public SearchImagesCmd.Exec createSearchImagesCmdExec() {
        return new SearchImagesCmdExec(getBaseResource());
    }

    @Override
    public RemoveImageCmd.Exec createRemoveImageCmdExec() {
        return new RemoveImageCmdExec(getBaseResource());
    }

    @Override
    public ListImagesCmd.Exec createListImagesCmdExec() {
        return new ListImagesCmdExec(getBaseResource());
    }

    @Override
    public InspectImageCmd.Exec createInspectImageCmdExec() {
        return new InspectImageCmdExec(getBaseResource());
    }

    @Override
    public ListContainersCmd.Exec createListContainersCmdExec() {
        return new ListContainersCmdExec(getBaseResource());
    }

    @Override
    public CreateContainerCmd.Exec createCreateContainerCmdExec() {
        return new CreateContainerCmdExec(getBaseResource());
    }

    @Override
    public StartContainerCmd.Exec createStartContainerCmdExec() {
        return new StartContainerCmdExec(getBaseResource());
    }

    @Override
    public InspectContainerCmd.Exec createInspectContainerCmdExec() {
        return new InspectContainerCmdExec(getBaseResource());
    }

    @Override
    public ExecCreateCmd.Exec createExecCmdExec() {
        return new ExecCreateCmdExec(getBaseResource());
    }

    @Override
    public RemoveContainerCmd.Exec createRemoveContainerCmdExec() {
        return new RemoveContainerCmdExec(getBaseResource());
    }

    @Override
    public WaitContainerCmd.Exec createWaitContainerCmdExec() {
        return new WaitContainerCmdExec(getBaseResource());
    }

    @Override
    public AttachContainerCmd.Exec createAttachContainerCmdExec() {
        return new AttachContainerCmdExec(getBaseResource());
    }

    @Override
    public ExecStartCmd.Exec createExecStartCmdExec() {
        return new ExecStartCmdExec(getBaseResource());
    }

    @Override
    public LogContainerCmd.Exec createLogContainerCmdExec() {
        return new LogContainerCmdExec(getBaseResource());
    }

    @Override
    public CopyFileFromContainerCmd.Exec createCopyFileFromContainerCmdExec() {
        return new CopyFileFromContainerCmdExec(getBaseResource());
    }

    @Override
    public StopContainerCmd.Exec createStopContainerCmdExec() {
        return new StopContainerCmdExec(getBaseResource());
    }

    @Override
    public ContainerDiffCmd.Exec createContainerDiffCmdExec() {
        return new ContainerDiffCmdExec(getBaseResource());
    }

    @Override
    public KillContainerCmd.Exec createKillContainerCmdExec() {
        return new KillContainerCmdExec(getBaseResource());
    }

    @Override
    public RestartContainerCmd.Exec createRestartContainerCmdExec() {
        return new RestartContainerCmdExec(getBaseResource());
    }

    @Override
    public CommitCmd.Exec createCommitCmdExec() {
        return new CommitCmdExec(getBaseResource());
    }

    @Override
    public BuildImageCmd.Exec createBuildImageCmdExec() {
        return new BuildImageCmdExec(getBaseResource());
    }

    @Override
    public TopContainerCmd.Exec createTopContainerCmdExec() {
        return new TopContainerCmdExec(getBaseResource());
    }

    @Override
    public TagImageCmd.Exec createTagImageCmdExec() {
        return new TagImageCmdExec(getBaseResource());
    }

    @Override
    public PauseContainerCmd.Exec createPauseContainerCmdExec() {
        return new PauseContainerCmdExec(getBaseResource());
    }

    @Override
    public UnpauseContainerCmd.Exec createUnpauseContainerCmdExec() {
        return new UnpauseContainerCmdExec(baseResource);
    }

    @Override
    public EventsCmd.Exec createEventsCmdExec() {
        return new EventsCmdExec(getBaseResource());
    }

    @Override
    public void close() throws IOException {
        checkNotNull(client, "Factory not initialized. You probably forgot to call init()!");
        client.close();
    }

}
