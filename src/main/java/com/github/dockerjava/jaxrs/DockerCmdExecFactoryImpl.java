package com.github.dockerjava.jaxrs;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.net.URI;

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
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.github.dockerjava.api.DockerClientException;
import com.github.dockerjava.api.command.AttachContainerCmd;
import com.github.dockerjava.api.command.AuthCmd;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.command.CommitCmd;
import com.github.dockerjava.api.command.ContainerDiffCmd;
import com.github.dockerjava.api.command.CopyFileFromContainerCmd;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateImageCmd;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.api.command.EventsCmd;
import com.github.dockerjava.api.command.ExecCreateCmd;
import com.github.dockerjava.api.command.ExecStartCmd;
import com.github.dockerjava.api.command.InfoCmd;
import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectExecCmd;
import com.github.dockerjava.api.command.InspectImageCmd;
import com.github.dockerjava.api.command.KillContainerCmd;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.command.PauseContainerCmd;
import com.github.dockerjava.api.command.PingCmd;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.PushImageCmd;
import com.github.dockerjava.api.command.RemoveContainerCmd;
import com.github.dockerjava.api.command.RemoveImageCmd;
import com.github.dockerjava.api.command.RestartContainerCmd;
import com.github.dockerjava.api.command.SaveImageCmd;
import com.github.dockerjava.api.command.SearchImagesCmd;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.command.StatsCmd.Exec;
import com.github.dockerjava.api.command.StopContainerCmd;
import com.github.dockerjava.api.command.TagImageCmd;
import com.github.dockerjava.api.command.TopContainerCmd;
import com.github.dockerjava.api.command.UnpauseContainerCmd;
import com.github.dockerjava.api.command.VersionCmd;
import com.github.dockerjava.api.command.WaitContainerCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.util.FollowRedirectsFilter;
import com.github.dockerjava.core.util.JsonClientFilter;
import com.github.dockerjava.core.util.ResponseStatusExceptionFilter;
import com.github.dockerjava.core.util.SelectiveLoggingFilter;
//import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
// see https://github.com/docker-java/docker-java/issues/196
import com.github.dockerjava.jaxrs.connector.ApacheConnectorProvider;

public class DockerCmdExecFactoryImpl implements DockerCmdExecFactory {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DockerCmdExecFactoryImpl.class.getName());
	private Client client;
	private WebTarget baseResource;

	public void init(DockerClientConfig dockerClientConfig) {
		checkNotNull(dockerClientConfig, "config was not specified");

		ClientConfig clientConfig = new ClientConfig();
		// clientConfig.connectorProvider(new ApacheConnectorProvider());
		// see #226
		// StatsCmd could create a live stream for one container. 
		// Unfortunately, ApacheConnector would perform a ChunkedInputStream call that results in the application blocking. 
		// The reason is org.apache.http.impl.io.ChunkedInputStream would NEVER closes the underlying stream, even when close
		// gets called.  Instead, it will read until the "end" of its chunking on close.
		// see <code>com.github.dockerjava.api.command.StatsCmdTest<code>
		clientConfig.connectorProvider(new HttpUrlConnectorProvider());
		clientConfig.property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE,
				true);

		clientConfig.register(ResponseStatusExceptionFilter.class);
		clientConfig.register(JsonClientFilter.class);
		clientConfig.register(JacksonJsonProvider.class);

		if (dockerClientConfig.followRedirectsFilterEnabled()) {
			clientConfig.register(FollowRedirectsFilter.class);
		}

		if (dockerClientConfig.isLoggingFilterEnabled()) {
			clientConfig.register(new SelectiveLoggingFilter(LOGGER, true));
		}

		if (dockerClientConfig.getReadTimeout() != null) {
			int readTimeout = dockerClientConfig.getReadTimeout();
			clientConfig.property(ClientProperties.READ_TIMEOUT, readTimeout);
		}

		URI originalUri = dockerClientConfig.getUri();

		SSLContext sslContext = null;

		if (dockerClientConfig.getSslConfig() != null) {
			try {
				sslContext = dockerClientConfig.getSslConfig().getSSLContext();
			} catch (Exception ex) {
				throw new DockerClientException("Error in SSL Configuration",
						ex);
			}
		}

		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
				getSchemeRegistry(originalUri, sslContext));

		if (dockerClientConfig.getMaxTotalConnections() != null)
			connManager
					.setMaxTotal(dockerClientConfig.getMaxTotalConnections());
		if (dockerClientConfig.getMaxPerRoutConnections() != null)
			connManager.setDefaultMaxPerRoute(dockerClientConfig
					.getMaxPerRoutConnections());

		clientConfig.property(ApacheClientProperties.CONNECTION_MANAGER,
				connManager);

		ClientBuilder clientBuilder = ClientBuilder.newBuilder().withConfig(
				clientConfig);

		if (sslContext != null) {
			clientBuilder.sslContext(sslContext);
		}

		client = clientBuilder.build();

		if (originalUri.getScheme().equals("unix")) {
			dockerClientConfig.setUri(UnixConnectionSocketFactory
					.sanitizeUri(originalUri));
		}
		WebTarget webResource = client.target(dockerClientConfig.getUri());

		if (dockerClientConfig.getVersion() == null
				|| dockerClientConfig.getVersion().isEmpty()) {
			baseResource = webResource;
		} else {
			baseResource = webResource.path("v"
					+ dockerClientConfig.getVersion());
		}
	}

	private org.apache.http.config.Registry<ConnectionSocketFactory> getSchemeRegistry(
			final URI originalUri, SSLContext sslContext) {
		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder
				.create();
		registryBuilder.register("http",
				PlainConnectionSocketFactory.getSocketFactory());
		if (sslContext != null) {
			registryBuilder.register("https", new SSLConnectionSocketFactory(
					sslContext));
		}
		registryBuilder.register("unix", new UnixConnectionSocketFactory(
				originalUri));
		return registryBuilder.build();
	}

	protected WebTarget getBaseResource() {
		checkNotNull(baseResource,
				"Factory not initialized. You probably forgot to call init()!");
		return baseResource;
	}

	public AuthCmd.Exec createAuthCmdExec() {
		return new AuthCmdExec(getBaseResource());
	}

	public InfoCmd.Exec createInfoCmdExec() {
		return new InfoCmdExec(getBaseResource());
	}

	public PingCmd.Exec createPingCmdExec() {
		return new PingCmdExec(getBaseResource());
	}

	public VersionCmd.Exec createVersionCmdExec() {
		return new VersionCmdExec(getBaseResource());
	}

	public PullImageCmd.Exec createPullImageCmdExec() {
		return new PullImageCmdExec(getBaseResource());
	}

	public PushImageCmd.Exec createPushImageCmdExec() {
		return new PushImageCmdExec(getBaseResource());
	}

	public SaveImageCmd.Exec createSaveImageCmdExec() {
		return new SaveImageCmdExec(getBaseResource());
	}

	public CreateImageCmd.Exec createCreateImageCmdExec() {
		return new CreateImageCmdExec(getBaseResource());
	}

	public SearchImagesCmd.Exec createSearchImagesCmdExec() {
		return new SearchImagesCmdExec(getBaseResource());
	}

	public RemoveImageCmd.Exec createRemoveImageCmdExec() {
		return new RemoveImageCmdExec(getBaseResource());
	}

	public ListImagesCmd.Exec createListImagesCmdExec() {
		return new ListImagesCmdExec(getBaseResource());
	}

	public InspectImageCmd.Exec createInspectImageCmdExec() {
		return new InspectImageCmdExec(getBaseResource());
	}

	public ListContainersCmd.Exec createListContainersCmdExec() {
		return new ListContainersCmdExec(getBaseResource());
	}

	public CreateContainerCmd.Exec createCreateContainerCmdExec() {
		return new CreateContainerCmdExec(getBaseResource());
	}

	public StartContainerCmd.Exec createStartContainerCmdExec() {
		return new StartContainerCmdExec(getBaseResource());
	}

	public InspectContainerCmd.Exec createInspectContainerCmdExec() {
		return new InspectContainerCmdExec(getBaseResource());
	}

	public ExecCreateCmd.Exec createExecCmdExec() {
		return new ExecCreateCmdExec(getBaseResource());
	}

	public RemoveContainerCmd.Exec createRemoveContainerCmdExec() {
		return new RemoveContainerCmdExec(getBaseResource());
	}

	public WaitContainerCmd.Exec createWaitContainerCmdExec() {
		return new WaitContainerCmdExec(getBaseResource());
	}

	public AttachContainerCmd.Exec createAttachContainerCmdExec() {
		return new AttachContainerCmdExec(getBaseResource());
	}

	public ExecStartCmd.Exec createExecStartCmdExec() {
		return new ExecStartCmdExec(getBaseResource());
	}

	public InspectExecCmd.Exec createInspectExecCmdExec() {
		return new InspectExecCmdExec(getBaseResource());
	}

	public LogContainerCmd.Exec createLogContainerCmdExec() {
		return new LogContainerCmdExec(getBaseResource());
	}

	public CopyFileFromContainerCmd.Exec createCopyFileFromContainerCmdExec() {
		return new CopyFileFromContainerCmdExec(getBaseResource());
	}

	public StopContainerCmd.Exec createStopContainerCmdExec() {
		return new StopContainerCmdExec(getBaseResource());
	}

	public ContainerDiffCmd.Exec createContainerDiffCmdExec() {
		return new ContainerDiffCmdExec(getBaseResource());
	}

	public KillContainerCmd.Exec createKillContainerCmdExec() {
		return new KillContainerCmdExec(getBaseResource());
	}

	public RestartContainerCmd.Exec createRestartContainerCmdExec() {
		return new RestartContainerCmdExec(getBaseResource());
	}

	public CommitCmd.Exec createCommitCmdExec() {
		return new CommitCmdExec(getBaseResource());
	}

	public BuildImageCmd.Exec createBuildImageCmdExec() {
		return new BuildImageCmdExec(getBaseResource());
	}

	public TopContainerCmd.Exec createTopContainerCmdExec() {
		return new TopContainerCmdExec(getBaseResource());
	}

	public TagImageCmd.Exec createTagImageCmdExec() {
		return new TagImageCmdExec(getBaseResource());
	}

	public PauseContainerCmd.Exec createPauseContainerCmdExec() {
		return new PauseContainerCmdExec(getBaseResource());
	}

	public UnpauseContainerCmd.Exec createUnpauseContainerCmdExec() {
		return new UnpauseContainerCmdExec(baseResource);
	}

	public EventsCmd.Exec createEventsCmdExec() {
		return new EventsCmdExec(getBaseResource());
	}

	public Exec createStatsCmdExec() {
		return new StatsCmdExec(getBaseResource());
	}

	public void close() throws IOException {
		checkNotNull(client,
				"Factory not initialized. You probably forgot to call init()!");
		client.close();
	}

}
