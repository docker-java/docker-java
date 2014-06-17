package com.github.dockerjava.client;

import static org.apache.commons.io.IOUtils.closeQuietly;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

import com.github.dockerjava.client.command.AbstrDockerCmd;
import com.github.dockerjava.client.command.AuthCmd;
import com.github.dockerjava.client.command.BuildImgCmd;
import com.github.dockerjava.client.command.CommitCmd;
import com.github.dockerjava.client.command.ContainerDiffCmd;
import com.github.dockerjava.client.command.CopyFileFromContainerCmd;
import com.github.dockerjava.client.command.CreateContainerCmd;
import com.github.dockerjava.client.command.ImportImageCmd;
import com.github.dockerjava.client.command.InfoCmd;
import com.github.dockerjava.client.command.InspectContainerCmd;
import com.github.dockerjava.client.command.InspectImageCmd;
import com.github.dockerjava.client.command.KillContainerCmd;
import com.github.dockerjava.client.command.ListContainersCmd;
import com.github.dockerjava.client.command.ListImagesCmd;
import com.github.dockerjava.client.command.AttachContainerCmd;
import com.github.dockerjava.client.command.LogContainerCmd;
import com.github.dockerjava.client.command.PullImageCmd;
import com.github.dockerjava.client.command.PushImageCmd;
import com.github.dockerjava.client.command.RemoveContainerCmd;
import com.github.dockerjava.client.command.RemoveImageCmd;
import com.github.dockerjava.client.command.RestartContainerCmd;
import com.github.dockerjava.client.command.SearchImagesCmd;
import com.github.dockerjava.client.command.StartContainerCmd;
import com.github.dockerjava.client.command.StopContainerCmd;
import com.github.dockerjava.client.command.TagImageCmd;
import com.github.dockerjava.client.command.TopContainerCmd;
import com.github.dockerjava.client.command.VersionCmd;
import com.github.dockerjava.client.command.WaitContainerCmd;
import com.github.dockerjava.client.model.AuthConfig;
import com.github.dockerjava.client.model.CreateContainerConfig;
import com.github.dockerjava.client.utils.JsonClientFilter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.ApacheHttpClient4Handler;

/**
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 */
public class DockerClient {

	private Client client;
	private WebResource baseResource;
	private AuthConfig authConfig;

	public DockerClient() throws DockerException {
		this(Config.createConfig());
	}

	public DockerClient(String serverUrl) throws DockerException {
		this(configWithServerUrl(serverUrl));
	}

	private static Config configWithServerUrl(String serverUrl)
			throws DockerException {
		final Config c = Config.createConfig();
		c.url = URI.create(serverUrl);
		return c;
	}

	private DockerClient(Config config) {
		// restEndpointUrl = config.url + "/v" + config.version;
		ClientConfig clientConfig = new DefaultClientConfig();
		// clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
		// Boolean.TRUE);

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", config.url.getPort(),
				PlainSocketFactory.getSocketFactory()));
		schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory
				.getSocketFactory()));

		PoolingClientConnectionManager cm = new PoolingClientConnectionManager(
				schemeRegistry);
		// Increase max total connection
		cm.setMaxTotal(1000);
		// Increase default max connection per route
		cm.setDefaultMaxPerRoute(1000);

		HttpClient httpClient = new DefaultHttpClient(cm);
		client = new ApacheHttpClient4(new ApacheHttpClient4Handler(httpClient,
				null, false), clientConfig);

		client.setReadTimeout(10000);
		// Experimental support for unix sockets:
		// client = new UnixSocketClient(clientConfig);

		client.addFilter(new JsonClientFilter());
		client.addFilter(new LoggingFilter());

		baseResource = client.resource(config.url + "/v" + config.version);
	}

	public void setCredentials(String username, String password, String email) {
		if (username == null) {
			throw new IllegalArgumentException("username is null");
		}
		if (password == null) {
			throw new IllegalArgumentException("password is null");
		}
		if (email == null) {
			throw new IllegalArgumentException("email is null");
		}
		authConfig = new AuthConfig();
		authConfig.setUsername(username);
		authConfig.setPassword(password);
		authConfig.setEmail(email);
	}

	public <RES_T> RES_T execute(AbstrDockerCmd<?, RES_T> command)
			throws DockerException {
		return command.withBaseResource(baseResource).exec();
	}

	public AuthConfig authConfig() throws DockerException {
		return authConfig != null ? authConfig : authConfigFromProperties();
	}

	private static AuthConfig authConfigFromProperties() throws DockerException {
		final AuthConfig a = new AuthConfig();

		a.setUsername(Config.createConfig().username);
		a.setPassword(Config.createConfig().password);
		a.setEmail(Config.createConfig().email);

		if (a.getUsername() == null) {
			throw new IllegalStateException("username is null");
		}
		if (a.getPassword() == null) {
			throw new IllegalStateException("password is null");
		}
		if (a.getEmail() == null) {
			throw new IllegalStateException("email is null");
		}

		return a;
	}

	/**
	 * * MISC API *
	 */

	/**
	 * Authenticate with the server, useful for checking authentication.
	 */
	public AuthCmd authCmd() {
		return new AuthCmd(authConfig()).withBaseResource(baseResource);
	}

	public InfoCmd infoCmd() throws DockerException {
		return new InfoCmd().withBaseResource(baseResource);
	}

	public VersionCmd versionCmd() throws DockerException {
		return new VersionCmd().withBaseResource(baseResource);
	}

	/**
	 * * IMAGE API *
	 */

	public PullImageCmd pullImageCmd(String repository) {
		return new PullImageCmd(repository).withBaseResource(baseResource);
	}

	public PushImageCmd pushImageCmd(String name) {
		return new PushImageCmd(name).withAuthConfig(authConfig())
				.withBaseResource(baseResource);
	}

//	public ClientResponse pushImage(String name) {
//		return execute(pushImageCmd(name));
//	}

	public ImportImageCmd importImageCmd(String repository,
			InputStream imageStream) {
		return new ImportImageCmd(repository, imageStream)
				.withBaseResource(baseResource);
	}

//	public ImageCreateResponse importImage(String repository,
//			InputStream imageStream) {
//		return execute(importImageCmd(repository, imageStream));
//	}

	public SearchImagesCmd searchImagesCmd(String term) {
		return new SearchImagesCmd(term).withBaseResource(baseResource);
	}

//	public List<SearchItem> searchImages(String term) {
//		return execute(searchImagesCmd(term));
//	}

	public RemoveImageCmd removeImageCmd(String imageId) {
		return new RemoveImageCmd(imageId).withBaseResource(baseResource);
	}

//	/**
//	 * Remove an image, deleting any tags it might have.
//	 */
//	public void removeImage(String imageId) {
//		execute(removeImageCmd(imageId));
//	}
//
//	public void removeImages(List<String> images) {
//		Preconditions.checkNotNull(images, "List of images can't be null");
//
//		for (String imageId : images) {
//			removeImage(imageId);
//		}
//	}

	public ListImagesCmd listImagesCmd() {
		return new ListImagesCmd().withBaseResource(baseResource);
	}

//	public List<Image> listImages() {
//		return execute(listImagesCmd());
//	}

	public InspectImageCmd inspectImageCmd(String imageId) {
		return new InspectImageCmd(imageId).withBaseResource(baseResource);
	}

//	public ImageInspectResponse inspectImage(String imageId) {
//		return execute(inspectImageCmd(imageId));
//	}

	/**
	 * * CONTAINER API *
	 */

	public ListContainersCmd listContainersCmd() {
		return new ListContainersCmd().withBaseResource(baseResource);
	}

//	public List<Container> listContainers() {
//		return execute(listContainersCmd());
//	}

	public CreateContainerCmd createContainerCmd(String image) {
		return new CreateContainerCmd(new CreateContainerConfig()).withImage(
				image).withBaseResource(baseResource);
	}

	public StartContainerCmd startContainerCmd(String containerId) {
		return new StartContainerCmd(containerId)
				.withBaseResource(baseResource);
	}

	public InspectContainerCmd inspectContainerCmd(String containerId) {
		return new InspectContainerCmd(containerId)
				.withBaseResource(baseResource);
	}

	public RemoveContainerCmd removeContainerCmd(String containerId) {
		return new RemoveContainerCmd(containerId)
				.withBaseResource(baseResource);
	}

	public WaitContainerCmd waitContainerCmd(String containerId) {
		return new WaitContainerCmd(containerId).withBaseResource(baseResource);
	}

	public AttachContainerCmd attachContainerCmd(String containerId) {
		return new AttachContainerCmd(containerId).withBaseResource(baseResource);
	}
	
	
	public LogContainerCmd logContainerCmd(String containerId) {
		return new LogContainerCmd(containerId).withBaseResource(baseResource);
	}

	public CopyFileFromContainerCmd copyFileFromContainerCmd(
			String containerId, String resource) {
		return new CopyFileFromContainerCmd(containerId, resource)
				.withBaseResource(baseResource);
	}

	public ContainerDiffCmd containerDiffCmd(String containerId) {
		return new ContainerDiffCmd(containerId).withBaseResource(baseResource);
	}

	public StopContainerCmd stopContainerCmd(String containerId) {
		return new StopContainerCmd(containerId).withBaseResource(baseResource);
	}

	public KillContainerCmd killContainerCmd(String containerId) {
		return new KillContainerCmd(containerId).withBaseResource(baseResource);
	}

	public RestartContainerCmd restartContainerCmd(String containerId) {
		return new RestartContainerCmd(containerId)
				.withBaseResource(baseResource);
	}

	public CommitCmd commitCmd(String containerId) {
		return new CommitCmd(containerId).withBaseResource(baseResource);
	}

	public BuildImgCmd buildImageCmd(File dockerFolder) {
		return new BuildImgCmd(dockerFolder).withBaseResource(baseResource);
	}

	public TopContainerCmd topContainerCmd(String containerId) {
		return new TopContainerCmd(containerId).withBaseResource(baseResource);
	}
	
	public TagImageCmd tagImageCmd(String imageId, String repository, String tag) {
		return new TagImageCmd(imageId, repository, tag).withBaseResource(baseResource);
	}
	

	/**
	 * @return The output slurped into a string.
	 */
	public static String asString(ClientResponse response) throws IOException {

		StringWriter out = new StringWriter();
		try {
			LineIterator itr = IOUtils.lineIterator(
					response.getEntityInputStream(), "UTF-8");
			while (itr.hasNext()) {
				String line = itr.next();
				out.write(line + (itr.hasNext() ? "\n" : ""));
			}
		} finally {
			closeQuietly(response.getEntityInputStream());
		}
		return out.toString();
	}
}
