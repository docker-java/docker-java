package com.github.dockerjava.client;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.github.dockerjava.client.command.*;
import com.github.dockerjava.client.model.AuthConfig;
import com.github.dockerjava.client.utils.JsonClientFilter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 */
public class DockerClient implements Closeable {

	private final Client client;

    private final CommandFactory cmdFactory;
    private final WebTarget baseResource;
    private final DockerClientConfig dockerClientConfig;

	public DockerClient() {
		this(DockerClientConfig.createDefaultConfigBuilder().build());
	}

	public DockerClient(String serverUrl) {
		this(configWithServerUrl(serverUrl));
	}

	private static DockerClientConfig configWithServerUrl(String serverUrl) {
		return DockerClientConfig.createDefaultConfigBuilder()
                .withUri(serverUrl)
                .build();
	}

    public DockerClient(DockerClientConfig dockerClientConfig) {
        this(dockerClientConfig, new DefaultCommandFactory());
    }

	public DockerClient(DockerClientConfig dockerClientConfig, CommandFactory cmdFactory) {
        this.cmdFactory = cmdFactory;
        this.dockerClientConfig = dockerClientConfig;

        ClientConfig clientConfig = new ClientConfig();

        if (dockerClientConfig.getReadTimeout() != null) {
            clientConfig.property(ClientProperties.READ_TIMEOUT, dockerClientConfig.getReadTimeout());
        }

        clientConfig.register(JsonClientFilter.class);
        clientConfig.register(JacksonJsonProvider.class);

        if (dockerClientConfig.isLoggingFilterEnabled()) {
            clientConfig.register(SelectiveLoggingFilter.class);
        }

        client = ClientBuilder.newBuilder().withConfig(clientConfig).build();
        WebTarget webResource = client.target(dockerClientConfig.getUri());

        if (dockerClientConfig.getVersion() != null) {
            baseResource = webResource.path("v" + dockerClientConfig.getVersion());
        } else {
            baseResource = webResource;
        }
    }

	public <RES_T> RES_T execute(AbstrDockerCmd<?, RES_T> command)
			throws DockerException {
		return command.withBaseResource(baseResource).exec();
	}

    public AuthConfig authConfig() throws DockerException {
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
		return cmdFactory.authCmd(authConfig()).withBaseResource(baseResource);
	}

	public InfoCmd infoCmd() throws DockerException {
		return cmdFactory.infoCmd().withBaseResource(baseResource);
	}

	public PingCmd pingCmd() {
	    return cmdFactory.pingCmd().withBaseResource(baseResource);
	}

	public VersionCmd versionCmd() throws DockerException {
		return cmdFactory.versionCmd().withBaseResource(baseResource);
	}

	/**
	 * * IMAGE API *
	 */

	public PullImageCmd pullImageCmd(String repository) {
		return cmdFactory.pullImageCmd(repository).withBaseResource(baseResource);
	}

	public PushImageCmd pushImageCmd(String name) {
		return cmdFactory.pushImageCmd(name).withAuthConfig(authConfig())
				.withBaseResource(baseResource);
	}

//	public ClientResponse pushImage(String name) {
//		return execute(pushImageCmd(name));
//	}

	public CreateImageCmd createImageCmd(String repository, InputStream imageStream) {
		return cmdFactory.createImageCmd(repository, imageStream)
				.withBaseResource(baseResource);
	}

	public SearchImagesCmd searchImagesCmd(String term) {
		return cmdFactory.searchImagesCmd(term).withBaseResource(baseResource);
	}

	public RemoveImageCmd removeImageCmd(String imageId) {
		return cmdFactory.removeImageCmd(imageId).withBaseResource(baseResource);
	}

	public ListImagesCmd listImagesCmd() {
		return cmdFactory.listImagesCmd().withBaseResource(baseResource);
	}

	public InspectImageCmd inspectImageCmd(String imageId) {
		return cmdFactory.inspectImageCmd(imageId).withBaseResource(baseResource);
	}

	/**
	 * * CONTAINER API *
	 */

	public ListContainersCmd listContainersCmd() {
		return cmdFactory.listContainersCmd().withBaseResource(baseResource);
	}

	public CreateContainerCmd createContainerCmd(String image) {
		return cmdFactory.createContainerCmd(image).withBaseResource(baseResource);
	}

	public StartContainerCmd startContainerCmd(String containerId) {
		return cmdFactory.startContainerCmd(containerId)
				.withBaseResource(baseResource);
	}

	public InspectContainerCmd inspectContainerCmd(String containerId) {
		return cmdFactory.inspectContainerCmd(containerId)
				.withBaseResource(baseResource);
	}

	public RemoveContainerCmd removeContainerCmd(String containerId) {
		return cmdFactory.removeContainerCmd(containerId)
				.withBaseResource(baseResource);
	}

	public WaitContainerCmd waitContainerCmd(String containerId) {
		return cmdFactory.waitContainerCmd(containerId).withBaseResource(baseResource);
	}

	public AttachContainerCmd attachContainerCmd(String containerId) {
		return cmdFactory.attachContainerCmd(containerId).withBaseResource(baseResource);
	}


	public LogContainerCmd logContainerCmd(String containerId) {
		return cmdFactory.logContainerCmd(containerId).withBaseResource(baseResource);
	}

	public CopyFileFromContainerCmd copyFileFromContainerCmd(
			String containerId, String resource) {
		return cmdFactory.copyFileFromContainerCmd(containerId, resource)
				.withBaseResource(baseResource);
	}

	public ContainerDiffCmd containerDiffCmd(String containerId) {
		return cmdFactory.containerDiffCmd(containerId).withBaseResource(baseResource);
	}

	public StopContainerCmd stopContainerCmd(String containerId) {
		return cmdFactory.stopContainerCmd(containerId).withBaseResource(baseResource);
	}

	public KillContainerCmd killContainerCmd(String containerId) {
		return cmdFactory.killContainerCmd(containerId).withBaseResource(baseResource);
	}

	public RestartContainerCmd restartContainerCmd(String containerId) {
		return cmdFactory.restartContainerCmd(containerId)
				.withBaseResource(baseResource);
	}

	public CommitCmd commitCmd(String containerId) {
		return cmdFactory.commitCmd(containerId).withBaseResource(baseResource);
	}

	public BuildImgCmd buildImageCmd(File dockerFolder) {
		return cmdFactory.buildImgCmd(dockerFolder).withBaseResource(baseResource);
	}

	public BuildImgCmd buildImageCmd(InputStream tarInputStream) {
		return cmdFactory.buildImgCmd(tarInputStream).withBaseResource(baseResource);
	}

	public TopContainerCmd topContainerCmd(String containerId) {
		return cmdFactory.topContainerCmd(containerId).withBaseResource(baseResource);
	}

	public TagImageCmd tagImageCmd(String imageId, String repository, String tag) {
		return cmdFactory.tagImageCmd(imageId, repository, tag).withBaseResource(baseResource);
	}

    // TODO This is only being used by the test code for logging. Is it really necessary?

    /**
     * @return The output slurped into a string.
     */
    public static String asString(Response response) throws IOException {

        StringWriter out = new StringWriter();
        InputStream is = response.readEntity(InputStream.class);
        try {
            LineIterator itr = IOUtils.lineIterator(is, "UTF-8");
            while (itr.hasNext()) {
                String line = itr.next();
                out.write(line + (itr.hasNext() ? "\n" : ""));
            }
        } finally {
            IOUtils.closeQuietly(is);
        }
        return out.toString();
    }

    @Override
    public void close() throws IOException {
        client.close();
    }

}
