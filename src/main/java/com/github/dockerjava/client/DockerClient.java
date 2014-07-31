package com.github.dockerjava.client;

import static org.apache.commons.io.IOUtils.closeQuietly;

import java.io.*;

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

import com.github.dockerjava.client.command.AbstrDockerCmd;
import com.github.dockerjava.client.command.AttachContainerCmd;
import com.github.dockerjava.client.command.AuthCmd;
import com.github.dockerjava.client.command.BuildImgCmd;
import com.github.dockerjava.client.command.CommitCmd;
import com.github.dockerjava.client.command.ContainerDiffCmd;
import com.github.dockerjava.client.command.CopyFileFromContainerCmd;
import com.github.dockerjava.client.command.CreateContainerCmd;
import com.github.dockerjava.client.command.CreateImageCmd;
import com.github.dockerjava.client.command.InfoCmd;
import com.github.dockerjava.client.command.InspectContainerCmd;
import com.github.dockerjava.client.command.InspectImageCmd;
import com.github.dockerjava.client.command.KillContainerCmd;
import com.github.dockerjava.client.command.ListContainersCmd;
import com.github.dockerjava.client.command.ListImagesCmd;
import com.github.dockerjava.client.command.LogContainerCmd;
import com.github.dockerjava.client.command.PingCmd;
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

	private Client client;

    private final CommandFactory cmdFactory;
	private final WebResource baseResource;
	private AuthConfig authConfig;


	public DockerClient() {
		this(Config.createDefaultConfigBuilder().build());
	}

	public DockerClient(String serverUrl) {
		this(configWithServerUrl(serverUrl));
	}


	private static Config configWithServerUrl(String serverUrl) {
		return Config.createDefaultConfigBuilder()
                .withUri(serverUrl)
                .build();
	}


    public DockerClient(Config config) {
        this(config, new DefaultCommandFactory());
    }

	public DockerClient(Config config, CommandFactory cmdFactory) {
        this.cmdFactory = cmdFactory;

        HttpClient httpClient = getPoolingHttpClient(config);
        ClientConfig clientConfig = new DefaultClientConfig();
		client = new ApacheHttpClient4(new ApacheHttpClient4Handler(httpClient,
				null, false), clientConfig);

		if(config.getReadTimeout() != null) {
			client.setReadTimeout(config.getReadTimeout());
		}

		client.addFilter(new JsonClientFilter());

		if (config.isLoggingFilterEnabled())
			client.addFilter(new SelectiveLoggingFilter());
		
		WebResource webResource = client.resource(config.getUri());
		
		if(config.getVersion() != null) {
			baseResource = webResource.path("v" + config.getVersion());
		} else {
			baseResource = webResource;
		}
	}


    private HttpClient getPoolingHttpClient(Config config) {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", config.getUri().getPort(),
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

        // TODO This should probably come from the Config used to create the DockerClient.
        Config defaultConfig = Config.createDefaultConfigBuilder().build();
		a.setUsername(defaultConfig.getUsername());
		a.setPassword(defaultConfig.getPassword());
		a.setEmail(defaultConfig.getEmail());

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

    @Override
    public void close() throws IOException {
        client.destroy();
    }

}
