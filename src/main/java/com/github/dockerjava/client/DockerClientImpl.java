package com.github.dockerjava.client;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.github.dockerjava.api.command.AttachContainerCmd;
import com.github.dockerjava.api.command.AuthCmd;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.command.CommitCmd;
import com.github.dockerjava.api.command.ContainerDiffCmd;
import com.github.dockerjava.api.command.CopyFileFromContainerCmd;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateImageCmd;
import com.github.dockerjava.api.command.InfoCmd;
import com.github.dockerjava.api.command.InspectContainerCmd;
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
import com.github.dockerjava.api.command.SearchImagesCmd;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.command.StopContainerCmd;
import com.github.dockerjava.api.command.TagImageCmd;
import com.github.dockerjava.api.command.TopContainerCmd;
import com.github.dockerjava.api.command.UnpauseContainerCmd;
import com.github.dockerjava.api.command.VersionCmd;
import com.github.dockerjava.api.command.WaitContainerCmd;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.client.command.AttachContainerCommand;
import com.github.dockerjava.client.command.AuthCommand;
import com.github.dockerjava.client.command.BuildImageCommand;
import com.github.dockerjava.client.command.CommitCommand;
import com.github.dockerjava.client.command.ContainerDiffCommand;
import com.github.dockerjava.client.command.CopyFileFromContainerCommand;
import com.github.dockerjava.client.command.CreateContainerCommand;
import com.github.dockerjava.client.command.CreateImageCommand;
import com.github.dockerjava.client.command.InfoCommand;
import com.github.dockerjava.client.command.InspectContainerCommand;
import com.github.dockerjava.client.command.InspectImageCommand;
import com.github.dockerjava.client.command.KillContainerCommand;
import com.github.dockerjava.client.command.ListContainersCommand;
import com.github.dockerjava.client.command.ListImagesCommand;
import com.github.dockerjava.client.command.LogContainerCommand;
import com.github.dockerjava.client.command.PauseContainerCommand;
import com.github.dockerjava.client.command.PingCommand;
import com.github.dockerjava.client.command.PullImageCommand;
import com.github.dockerjava.client.command.PushImageCommand;
import com.github.dockerjava.client.command.RemoveContainerCommand;
import com.github.dockerjava.client.command.RemoveImageCommand;
import com.github.dockerjava.client.command.RestartContainerCommand;
import com.github.dockerjava.client.command.SearchImagesCommand;
import com.github.dockerjava.client.command.StartContainerCommand;
import com.github.dockerjava.client.command.StopContainerCommand;
import com.github.dockerjava.client.command.TagImageCommand;
import com.github.dockerjava.client.command.TopContainerCommand;
import com.github.dockerjava.client.command.UnpauseContainerCommand;
import com.github.dockerjava.client.command.VersionCommand;
import com.github.dockerjava.client.command.WaitContainerCommand;
import com.github.dockerjava.client.utils.JsonClientFilter;

/**
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 */
public class DockerClientImpl implements Closeable, com.github.dockerjava.api.DockerClient {

	private final Client client;

    private final WebTarget baseResource;
    private final DockerClientConfig dockerClientConfig;

	public DockerClientImpl() {
		this(DockerClientConfig.createDefaultConfigBuilder().build());
	}

	public DockerClientImpl(String serverUrl) {
		this(configWithServerUrl(serverUrl));
	}

	private static DockerClientConfig configWithServerUrl(String serverUrl) {
		return DockerClientConfig.createDefaultConfigBuilder()
                .withUri(serverUrl)
                .build();
	}

    public DockerClientImpl(DockerClientConfig dockerClientConfig) {
        this.dockerClientConfig = dockerClientConfig;

        ClientConfig clientConfig = new ClientConfig();

        clientConfig.register(JsonClientFilter.class);
        clientConfig.register(JacksonJsonProvider.class);

        if (dockerClientConfig.isLoggingFilterEnabled()) {
            clientConfig.register(SelectiveLoggingFilter.class);
        }

        if (dockerClientConfig.getReadTimeout() != null) {
        	int readTimeout = dockerClientConfig.getReadTimeout();
        	clientConfig.property(ClientProperties.READ_TIMEOUT, readTimeout);
        }
        client = ClientBuilder.newClient(clientConfig);
        
        
        
        WebTarget webResource = client.target(dockerClientConfig.getUri());

        if (dockerClientConfig.getVersion() != null) {
            baseResource = webResource.path("v" + dockerClientConfig.getVersion());
        } else {
            baseResource = webResource;
        }
    }

    public AuthConfig authConfig() {
        checkNotNull(dockerClientConfig.getUsername(), "Configured username is null.");
        checkNotNull(dockerClientConfig.getPassword(), "Configured password is null.");
        checkNotNull(dockerClientConfig.getEmail(), "Configured email is null.");

        AuthConfig authConfig = new AuthConfig();
        authConfig.setUsername(dockerClientConfig.getUsername());
        authConfig.setPassword(dockerClientConfig.getPassword());
        authConfig.setEmail(dockerClientConfig.getEmail());
        // TODO Make the registry address configurable
		return authConfig;
	}

	/**
	 * * MISC API *
	 */

	/**
	 * Authenticate with the server, useful for checking authentication.
	 */
	public AuthCmd authCmd() {
		return new AuthCommand(authConfig()).withBaseResource(baseResource);
	}

	public InfoCmd infoCmd() {
		return new InfoCommand().withBaseResource(baseResource);
	}

	public PingCmd pingCmd() {
	    return new PingCommand().withBaseResource(baseResource);
	}

	public VersionCmd versionCmd() {
		return new VersionCommand().withBaseResource(baseResource);
	}

	/**
	 * * IMAGE API *
	 */

	public PullImageCmd pullImageCmd(String repository) {
		return new PullImageCommand(repository).withBaseResource(baseResource);
	}

	public PushImageCmd pushImageCmd(String name) {
		return new PushImageCommand(name).withAuthConfig(authConfig())
				.withBaseResource(baseResource);
	}

	public CreateImageCmd createImageCmd(String repository, InputStream imageStream) {
		return new CreateImageCommand(repository, imageStream)
				.withBaseResource(baseResource);
	}

	public SearchImagesCmd searchImagesCmd(String term) {
		return new SearchImagesCommand(term).withBaseResource(baseResource);
	}

	public RemoveImageCmd removeImageCmd(String imageId) {
		return new RemoveImageCommand(imageId).withBaseResource(baseResource);
	}

	public ListImagesCmd listImagesCmd() {
		return new ListImagesCommand().withBaseResource(baseResource);
	}

	public InspectImageCmd inspectImageCmd(String imageId) {
		return new InspectImageCommand(imageId).withBaseResource(baseResource);
	}

	/**
	 * * CONTAINER API *
	 */

	public ListContainersCmd listContainersCmd() {
		return new ListContainersCommand().withBaseResource(baseResource);
	}

	public CreateContainerCmd createContainerCmd(String image) {
		return new CreateContainerCommand(image).withBaseResource(baseResource);
	}

	public StartContainerCmd startContainerCmd(String containerId) {
		return new StartContainerCommand(containerId)
				.withBaseResource(baseResource);
	}

	public InspectContainerCmd inspectContainerCmd(String containerId) {
		return new InspectContainerCommand(containerId)
				.withBaseResource(baseResource);
	}

	public RemoveContainerCmd removeContainerCmd(String containerId) {
		return new RemoveContainerCommand(containerId)
				.withBaseResource(baseResource);
	}

	public WaitContainerCmd waitContainerCmd(String containerId) {
		return new WaitContainerCommand(containerId).withBaseResource(baseResource);
	}

	public AttachContainerCmd attachContainerCmd(String containerId) {
		return new AttachContainerCommand(containerId).withBaseResource(baseResource);
	}

	public LogContainerCmd logContainerCmd(String containerId) {
		return new LogContainerCommand(containerId).withBaseResource(baseResource);
	}

	public CopyFileFromContainerCmd copyFileFromContainerCmd(
			String containerId, String resource) {
		return new CopyFileFromContainerCommand(containerId, resource)
				.withBaseResource(baseResource);
	}

	public ContainerDiffCmd containerDiffCmd(String containerId) {
		return new ContainerDiffCommand(containerId).withBaseResource(baseResource);
	}

	public StopContainerCmd stopContainerCmd(String containerId) {
		return new StopContainerCommand(containerId).withBaseResource(baseResource);
	}

	public KillContainerCmd killContainerCmd(String containerId) {
		return new KillContainerCommand(containerId).withBaseResource(baseResource);
	}

	public RestartContainerCmd restartContainerCmd(String containerId) {
		return new RestartContainerCommand(containerId)
				.withBaseResource(baseResource);
	}

	public CommitCmd commitCmd(String containerId) {
		return new CommitCommand(containerId).withBaseResource(baseResource);
	}

	public BuildImageCmd buildImageCmd(File dockerFolder) {
		return new BuildImageCommand(dockerFolder).withBaseResource(baseResource);
	}

	public BuildImageCmd buildImageCmd(InputStream tarInputStream) {
		return new BuildImageCommand(tarInputStream).withBaseResource(baseResource);
	}

	public TopContainerCmd topContainerCmd(String containerId) {
		return new TopContainerCommand(containerId).withBaseResource(baseResource);
	}

	public TagImageCmd tagImageCmd(String imageId, String repository, String tag) {
		return new TagImageCommand(imageId, repository, tag).withBaseResource(baseResource);
	}

	@Override
	public PauseContainerCmd pauseContainerCmd(String containerId) {
		return new PauseContainerCommand(containerId).withBaseResource(baseResource);
	}
	
	@Override
	public UnpauseContainerCmd unpauseContainerCmd(String containerId) {
		return new UnpauseContainerCommand(containerId).withBaseResource(baseResource);
	}

    @Override
    public void close() throws IOException {
        client.close();
    }

}
