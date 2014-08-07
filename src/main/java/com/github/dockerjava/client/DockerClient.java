package com.github.dockerjava.client;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.io.IOUtils.closeQuietly;

import java.io.*;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.client.command.*;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

import com.github.dockerjava.client.model.AuthConfig;
import com.github.dockerjava.client.utils.JsonClientFilter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.ApacheHttpClient4Handler;

/**
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 */
public class DockerClient implements Closeable {

	private final Client client;

    private final CommandFactory cmdFactory;
	private final WebResource baseResource;
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

        HttpClient httpClient = getPoolingHttpClient(dockerClientConfig);
        ClientConfig clientConfig = new DefaultClientConfig();
		client = new ApacheHttpClient4(new ApacheHttpClient4Handler(httpClient,
				null, false), clientConfig);

		if(dockerClientConfig.getReadTimeout() != null) {
			client.setReadTimeout(dockerClientConfig.getReadTimeout());
		}

		client.addFilter(new JsonClientFilter());

		if (dockerClientConfig.isLoggingFilterEnabled())
			client.addFilter(new SelectiveLoggingFilter());

		WebResource webResource = client.resource(dockerClientConfig.getUri());

		if(dockerClientConfig.getVersion() != null) {
			baseResource = webResource.path("v" + dockerClientConfig.getVersion());
		} else {
			baseResource = webResource;
		}
	}

    private HttpClient getPoolingHttpClient(DockerClientConfig dockerClientConfig) {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", dockerClientConfig.getUri().getPort(),
                PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory
                .getSocketFactory()));

        PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
        // Increase max total connection
        cm.setMaxTotal(1000);
        // Increase default max connection per route
        cm.setDefaultMaxPerRoute(1000);

        return new DefaultHttpClient(cm);
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
	public static String asString(InputStream response) throws IOException {

		StringWriter out = new StringWriter();
		try {
			LineIterator itr = IOUtils.lineIterator(
					response, "UTF-8");
			while (itr.hasNext()) {
				String line = itr.next();
				out.write(line + (itr.hasNext() ? "\n" : ""));
			}
		} finally {
			closeQuietly(response);
		}
		return out.toString();
	}

    @Override
    public void close() throws IOException {
        client.destroy();
    }

}
