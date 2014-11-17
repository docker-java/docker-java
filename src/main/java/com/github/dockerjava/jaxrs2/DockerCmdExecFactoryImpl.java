package com.github.dockerjava.jaxrs2;


import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.github.dockerjava.api.DockerClientException;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.core.CertificateUtils;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs2.util.JsonClientFilter;
import com.github.dockerjava.jaxrs2.util.ResponseStatusExceptionFilter;
import com.github.dockerjava.jaxrs2.util.SelectiveLoggingFilter;
import com.google.common.base.Preconditions;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.IOException;
import java.security.KeyStore;
import java.security.Security;
import java.util.logging.Logger;

public class DockerCmdExecFactoryImpl implements DockerCmdExecFactory {

    private static final Logger LOGGER = Logger.getLogger(DockerCmdExecFactoryImpl.class.getName());
    private Client client;
    private WebTarget baseResource;

    @Override
    public void init(DockerClientConfig dockerClientConfig) {
        Preconditions.checkNotNull(dockerClientConfig, "config was not specified");

        ClientConfig clientConfig = new ClientConfig();
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

        ClientBuilder clientBuilder = ClientBuilder.newBuilder().withConfig(clientConfig);

        String dockerCertPath = dockerClientConfig.getDockerCertPath();

        if (dockerCertPath != null) {
            boolean certificatesExist = CertificateUtils.verifyCertificatesExist(dockerCertPath);

            if (certificatesExist) {

                try {

                    Security.addProvider(new BouncyCastleProvider());

                    KeyStore keyStore = CertificateUtils.createKeyStore(dockerCertPath);
                    KeyStore trustStore = CertificateUtils.createTrustStore(dockerCertPath);

                    // properties acrobatics not needed for java > 1.6
                    String httpProtocols = System.getProperty("https.protocols");
                    System.setProperty("https.protocols", "TLSv1");
                    SslConfigurator sslConfig = SslConfigurator.newInstance(true);
                    if (httpProtocols != null) System.setProperty("https.protocols", httpProtocols);

                    sslConfig.keyStore(keyStore);
                    sslConfig.keyStorePassword("docker");
                    sslConfig.trustStore(trustStore);

                    SSLContext sslContext = sslConfig.createSSLContext();


                    clientBuilder.sslContext(sslContext);

                } catch (Exception e) {
                    throw new DockerClientException(e.getMessage(), e);
                }

            }

        }

        client = clientBuilder.build();

        WebTarget webResource = client.target(dockerClientConfig.getUri());

        if (dockerClientConfig.getVersion() == null || dockerClientConfig.getVersion().isEmpty()) {
            baseResource = webResource;
        } else {
            baseResource = webResource.path("v" + dockerClientConfig.getVersion());
        }

    }

    protected WebTarget getBaseResource() {
        Preconditions.checkNotNull(baseResource, "Factory not initialized. You probably forgot to call init()!");
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
        Preconditions.checkNotNull(client, "Factory not initialized. You probably forgot to call init()!");
        client.close();
    }

}
