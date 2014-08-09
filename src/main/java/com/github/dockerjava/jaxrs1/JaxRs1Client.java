package com.github.dockerjava.jaxrs1;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.io.IOUtils.closeQuietly;

import java.io.*;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.DockerException;
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
import com.github.dockerjava.client.DockerClientConfig;
import com.github.dockerjava.client.SelectiveLoggingFilter;
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
import com.github.dockerjava.jaxrs1.command.AttachContainerCommand;
import com.github.dockerjava.jaxrs1.command.AuthCommand;
import com.github.dockerjava.jaxrs1.command.BuildImageCommand;
import com.github.dockerjava.jaxrs1.command.CommitCommand;
import com.github.dockerjava.jaxrs1.command.ContainerDiffCommand;
import com.github.dockerjava.jaxrs1.command.CopyFileFromContainerCommand;
import com.github.dockerjava.jaxrs1.command.CreateContainerCommand;
import com.github.dockerjava.jaxrs1.command.CreateImageCommand;
import com.github.dockerjava.jaxrs1.command.InfoCommand;
import com.github.dockerjava.jaxrs1.command.InspectContainerCommand;
import com.github.dockerjava.jaxrs1.command.InspectImageCommand;
import com.github.dockerjava.jaxrs1.command.KillContainerCommand;
import com.github.dockerjava.jaxrs1.command.ListContainersCommand;
import com.github.dockerjava.jaxrs1.command.ListImagesCommand;
import com.github.dockerjava.jaxrs1.command.LogContainerCommand;
import com.github.dockerjava.jaxrs1.command.PauseContainerCommand;
import com.github.dockerjava.jaxrs1.command.PingCommand;
import com.github.dockerjava.jaxrs1.command.PullImageCommand;
import com.github.dockerjava.jaxrs1.command.PushImageCommand;
import com.github.dockerjava.jaxrs1.command.RemoveContainerCommand;
import com.github.dockerjava.jaxrs1.command.RemoveImageCommand;
import com.github.dockerjava.jaxrs1.command.RestartContainerCommand;
import com.github.dockerjava.jaxrs1.command.SearchImagesCommand;
import com.github.dockerjava.jaxrs1.command.StartContainerCommand;
import com.github.dockerjava.jaxrs1.command.StopContainerCommand;
import com.github.dockerjava.jaxrs1.command.TagImageCommand;
import com.github.dockerjava.jaxrs1.command.TopContainerCommand;
import com.github.dockerjava.jaxrs1.command.UnpauseContainerCommand;
import com.github.dockerjava.jaxrs1.command.VersionCommand;
import com.github.dockerjava.jaxrs1.command.WaitContainerCommand;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.ApacheHttpClient4Handler;

/**
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 */
public class JaxRs1Client implements DockerClient {

	private final Client client;
	private final WebResource baseResource;
	private final DockerClientConfig config;

	public JaxRs1Client() {
		this(DockerClientConfig.createDefaultConfigBuilder().build());
	}

	public JaxRs1Client(String serverUrl) {
		this(configWithServerUrl(serverUrl));
	}

	private static DockerClientConfig configWithServerUrl(String serverUrl) {
		return DockerClientConfig.createDefaultConfigBuilder()
				.withUri(serverUrl).build();
	}

	public JaxRs1Client(DockerClientConfig config) {
		this.config = config;

		HttpClient httpClient = getPoolingHttpClient(config);
		ClientConfig clientConfig = new DefaultClientConfig();
		client = new ApacheHttpClient4(new ApacheHttpClient4Handler(httpClient,
				null, false), clientConfig);

		if (config.getReadTimeout() != null) {
			client.setReadTimeout(config.getReadTimeout());
		}

		client.addFilter(new JsonClientFilter());

		if (config.isLoggingFilterEnabled())
			client.addFilter(new SelectiveLoggingFilter());

		WebResource webResource = client.resource(config.getUri());

		if (config.getVersion() != null) {
			baseResource = webResource.path("v" + config.getVersion());
		} else {
			baseResource = webResource;
		}

	}

	private HttpClient getPoolingHttpClient(
			DockerClientConfig dockerClientConfig) {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", dockerClientConfig.getUri()
				.getPort(), PlainSocketFactory.getSocketFactory()));
		schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory
				.getSocketFactory()));

		PoolingClientConnectionManager cm = new PoolingClientConnectionManager(
				schemeRegistry);
		// Increase max total connection
		cm.setMaxTotal(1000);
		// Increase default max connection per route
		cm.setDefaultMaxPerRoute(1000);

		return new DefaultHttpClient(cm);
	}

	@Override
	public AuthConfig authConfig() throws DockerException {
		checkNotNull(config.getUsername(),
				"Configured username is null.");
		checkNotNull(config.getPassword(),
				"Configured password is null.");
		checkNotNull(config.getEmail(), "Configured email is null.");

		AuthConfig authConfig = new AuthConfig();
		authConfig.setUsername(config.getUsername());
		authConfig.setPassword(config.getPassword());
		authConfig.setEmail(config.getEmail());
		// TODO Make the registry address configurable
		return authConfig;
	}

    @Override
    public AttachContainerCmd attachContainerCmd(String containerId) {
        return new AttachContainerCommand(containerId).withBaseResource(baseResource);
    }

    @Override
    public AuthCmd authCmd() {
        return new AuthCommand(authConfig()).withBaseResource(baseResource);
    }
    
    @Override
    public BuildImageCmd buildImageCmd(File dockerFolder) {
        return new BuildImageCommand(dockerFolder).withBaseResource(baseResource);
    }

    @Override
    public BuildImageCmd buildImageCmd(InputStream tarInputStream) {
        return new BuildImageCommand(tarInputStream).withBaseResource(baseResource);
    }

    @Override
    public CommitCmd commitCmd(String containerId) {
        return new CommitCommand(containerId).withBaseResource(baseResource);
    }

    @Override
    public ContainerDiffCmd containerDiffCmd(String containerId) {
        return new ContainerDiffCommand(containerId).withBaseResource(baseResource);
    }

    @Override
    public CopyFileFromContainerCmd copyFileFromContainerCmd(String containerId, String resource) {
        return new CopyFileFromContainerCommand(containerId, resource).withBaseResource(baseResource);
    }

    @Override
    public CreateContainerCmd createContainerCmd(String image) {
        return new CreateContainerCommand(image).withBaseResource(baseResource);
    }

    @Override
    public CreateImageCmd createImageCmd(String repository, InputStream imageStream) {
        return new CreateImageCommand(repository, imageStream).withBaseResource(baseResource);
    }

    @Override
    public InfoCmd infoCmd() {
        return new InfoCommand().withBaseResource(baseResource);
    }

    @Override
    public InspectContainerCmd inspectContainerCmd(String containerId) {
        return new InspectContainerCommand(containerId).withBaseResource(baseResource);
    }

    @Override
    public InspectImageCmd inspectImageCmd(String imageId) {
        return new InspectImageCommand(imageId).withBaseResource(baseResource);
    }

    @Override
    public KillContainerCmd killContainerCmd(String containerId) {
        return new KillContainerCommand(containerId).withBaseResource(baseResource);
    }

    @Override
    public ListContainersCmd listContainersCmd() {
        return new ListContainersCommand().withBaseResource(baseResource);
    }

    @Override
    public ListImagesCmd listImagesCmd() {
        return new ListImagesCommand().withBaseResource(baseResource);
    }

    @Override
    public LogContainerCmd logContainerCmd(String containerId) {
        return new LogContainerCommand(containerId).withBaseResource(baseResource);
    }

    @Override
    public PauseContainerCmd pauseContainerCmd(String containerId) {
        return new PauseContainerCommand(containerId).withBaseResource(baseResource);
    }

    @Override
    public PullImageCmd pullImageCmd(String repository) {
        return new PullImageCommand(repository).withBaseResource(baseResource);
    }

    @Override
    public PushImageCmd pushImageCmd(String imageName) {
        return new PushImageCommand(imageName).withBaseResource(baseResource);
    }

    @Override
    public RemoveContainerCmd removeContainerCmd(String containerId) {
        return new RemoveContainerCommand(containerId).withBaseResource(baseResource);
    }

    @Override
    public RemoveImageCmd removeImageCmd(String imageId) {
        return new RemoveImageCommand(imageId).withBaseResource(baseResource);
    }

    @Override
    public RestartContainerCmd restartContainerCmd(String containerId) {
        return new RestartContainerCommand(containerId).withBaseResource(baseResource);
    }

    @Override
    public SearchImagesCmd searchImagesCmd(String searchTerm) {
        return new SearchImagesCommand(searchTerm).withBaseResource(baseResource);
    }

    @Override
    public StartContainerCmd startContainerCmd(String containerId) {
        return new StartContainerCommand(containerId).withBaseResource(baseResource);
    }

    @Override
    public StopContainerCmd stopContainerCmd(String containerId) {
        return new StopContainerCommand(containerId).withBaseResource(baseResource);
    }

    @Override
    public TagImageCmd tagImageCmd(String imageId, String repository, String tag) {
        return new TagImageCommand(imageId, repository, tag).withBaseResource(baseResource);
    }

    @Override
    public TopContainerCmd topContainerCmd(String containerId) {
        return new TopContainerCommand(containerId).withBaseResource(baseResource);
    }

    @Override
    public UnpauseContainerCmd unpauseContainerCmd(String containerId) {
        return new UnpauseContainerCommand(containerId).withBaseResource(baseResource);
    }

    @Override
    public VersionCmd versionCmd() {
        return new VersionCommand().withBaseResource(baseResource);
    }

    @Override
    public WaitContainerCmd waitContainerCmd(String containerId) {
        return new WaitContainerCommand(containerId).withBaseResource(baseResource);
    }

    @Override
    public PingCmd pingCmd() {
    	return new PingCommand().withBaseResource(baseResource);
    }
    
    @Override
    public void close() throws IOException {
    	client.destroy();
    }

	// TODO This is only being used by the test code for logging. Is it really
	// necessary?
	/**
	 * @return The output slurped into a string.
	 */
	public static String asString(InputStream response) throws IOException {

		StringWriter out = new StringWriter();
		try {
			LineIterator itr = IOUtils.lineIterator(response, "UTF-8");
			while (itr.hasNext()) {
				String line = itr.next();
				out.write(line + (itr.hasNext() ? "\n" : ""));
			}
		} finally {
			closeQuietly(response);
		}
		return out.toString();
	}

	

}
