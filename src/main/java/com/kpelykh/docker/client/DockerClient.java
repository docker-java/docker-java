package com.kpelykh.docker.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import com.kpelykh.docker.client.model.*;
import com.kpelykh.docker.client.utils.CompressArchiveUtil;
import com.kpelykh.docker.client.utils.JsonClientFilter;
import com.sun.jersey.api.client.*;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.ApacheHttpClient4Handler;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.apache.commons.io.IOUtils.closeQuietly;

/**
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 */
public class DockerClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(DockerClient.class);

    private Client client;
	private String restEndpointUrl;
	private AuthConfig authConfig;

	public DockerClient() throws DockerException {
		this(Config.createConfig());
	}

	public DockerClient(String serverUrl) throws DockerException {
        this(configWithServerUrl(serverUrl));
    }

    private static Config configWithServerUrl(String serverUrl) throws DockerException {
        final Config c = Config.createConfig();
        c.url = URI.create(serverUrl);
        return c;
    }

    private DockerClient(Config config) {
		restEndpointUrl = config.url + "/v" + config.version;
		ClientConfig clientConfig = new DefaultClientConfig();
		//clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", config.url.getPort(), PlainSocketFactory.getSocketFactory()));
		schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));

		PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
		// Increase max total connection
		cm.setMaxTotal(1000);
		// Increase default max connection per route
		cm.setDefaultMaxPerRoute(1000);

		HttpClient httpClient = new DefaultHttpClient(cm);
		client = new ApacheHttpClient4(new ApacheHttpClient4Handler(httpClient, null, false), clientConfig);

		client.setReadTimeout(10000);
		//Experimental support for unix sockets:
		//client = new UnixSocketClient(clientConfig);

		client.addFilter(new JsonClientFilter());
		client.addFilter(new LoggingFilter());
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

	/**
	 * Authenticate with the server, useful for checking authentication.
	 */
	public void auth() throws DockerException {
		try {
			client.resource(restEndpointUrl + "/auth")
					.header("Content-Type", MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.post(authConfig());
		} catch (UniformInterfaceException e) {
			throw new DockerException(e);
		}
	}

	private String registryAuth() throws DockerException {
		try {
			return Base64.encodeBase64String(new ObjectMapper().writeValueAsString(authConfig()).getBytes());
		} catch (IOException e) {
			throw new DockerException(e);
		}
	}

    public AuthConfig authConfig() throws DockerException {
        return authConfig != null
                ? authConfig
                : authConfigFromProperties();
    }

    private static AuthConfig authConfigFromProperties() throws DockerException {
        final AuthConfig a = new AuthConfig();

        a.setUsername(Config.createConfig().username);
        a.setPassword(Config.createConfig().password);
        a.setEmail(Config.createConfig().email);

        if (a.getUsername() == null) {throw new IllegalStateException("username is null");}
        if (a.getPassword() == null) {throw new IllegalStateException("password is null");}
        if (a.getEmail() == null) {throw new IllegalStateException("email is null");}

        return a;
    }


    /**
	 * * MISC API
	 * *
	 */

	public Info info() throws DockerException {
		WebResource webResource = client.resource(restEndpointUrl + "/info");

		try {
			LOGGER.trace("GET: {}", webResource);
			return webResource.accept(MediaType.APPLICATION_JSON).get(Info.class);
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error.", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}


	public Version version() throws DockerException {
		WebResource webResource = client.resource(restEndpointUrl + "/version");

		try {
			LOGGER.trace("GET: {}", webResource);
			return webResource.accept(MediaType.APPLICATION_JSON).get(Version.class);
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error.", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}
	
	
	public int ping() throws DockerException {
		WebResource webResource = client.resource(restEndpointUrl + "/_ping");
	    
		try {
			LOGGER.trace("GET: {}", webResource);
			ClientResponse resp = webResource.get(ClientResponse.class);
			return resp.getStatus();
		} catch (UniformInterfaceException exception) {
			throw new DockerException(exception);
		}
	}


	/**
	 * * IMAGE API
	 * *
	 */

	public ClientResponse pull(String repository) throws DockerException {
		return this.pull(repository, null, null);
	}

	public ClientResponse pull(String repository, String tag) throws DockerException {
		return this.pull(repository, tag, null);
	}

	public ClientResponse pull(String repository, String tag, String registry) throws DockerException {
		Preconditions.checkNotNull(repository, "Repository was not specified");

		if (StringUtils.countMatches(repository, ":") == 1) {
			String repositoryTag[] = StringUtils.split(repository, ':');
			repository = repositoryTag[0];
			tag = repositoryTag[1];

		}

		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("tag", tag);
		params.add("fromImage", repository);
		params.add("registry", registry);

		WebResource webResource = client.resource(restEndpointUrl + "/images/create").queryParams(params);

		try {
			LOGGER.trace("POST: {}", webResource);
			return webResource.accept(MediaType.APPLICATION_OCTET_STREAM_TYPE).post(ClientResponse.class);
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error.", exception);
			} else {
				throw new DockerException(exception);
			}
		}
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

	/**
	 * Push the latest image to the repository.
	 *
	 * @param name The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
	 */
	public ClientResponse push(final String name) throws DockerException {
		if (name == null) {
			throw new IllegalArgumentException("name is null");
		}
		try {
			final String registryAuth = registryAuth();
			return client.resource(restEndpointUrl + "/images/" + name(name) + "/push")
					.header("X-Registry-Auth", registryAuth)
					.accept(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class);
		} catch (UniformInterfaceException e) {
			throw new DockerException(e);
		}
	}

	private String name(String name) {
		return name.contains("/") ? name : authConfig.getUsername();
	}

	/**
	 * Tag an image into a repository
	 *
	 * @param image       the local image to tag (either a name or an id)
	 * @param repository  the repository to tag in
	 * @param tag         any tag for this image
	 * @param force       (not documented)
	 * @return the HTTP status code (201 for success)
	 */
	public int tag(String image, String repository, String tag, boolean force) throws DockerException {
		Preconditions.checkNotNull(image, "image was not specified");
		Preconditions.checkNotNull(repository, "repository was not specified");
		Preconditions.checkNotNull(tag, " tag was not provided");

		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("repo", repository);
		params.add("tag", tag);
		params.add("force", String.valueOf(force));

		WebResource webResource = client.resource(restEndpointUrl + "/images/" + image + "/tag").queryParams(params);
	    
		try {
			LOGGER.trace("POST: {}", webResource);
			ClientResponse resp = webResource.post(ClientResponse.class);
			return resp.getStatus();
		} catch (UniformInterfaceException exception) {
			throw new DockerException(exception);
		}
	}

	/**
	 * Create an image by importing the given stream of a tar file.
	 *
	 * @param repository  the repository to import to
	 * @param tag         any tag for this image
	 * @param imageStream the InputStream of the tar file
	 * @return an {@link ImageCreateResponse} containing the id of the imported image
	 * @throws DockerException if the import fails for some reason.
	 */
	public ImageCreateResponse importImage(String repository, String tag, InputStream imageStream) throws DockerException {
		Preconditions.checkNotNull(repository, "Repository was not specified");
		Preconditions.checkNotNull(imageStream, "imageStream was not provided");

		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("repo", repository);
		params.add("tag", tag);
		params.add("fromSrc", "-");

		WebResource webResource = client.resource(restEndpointUrl + "/images/create").queryParams(params);

		try {
			LOGGER.trace("POST: {}", webResource);
			return webResource.accept(MediaType.APPLICATION_OCTET_STREAM_TYPE).post(ImageCreateResponse.class, imageStream);

		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error.", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}

	public List<SearchItem> search(String search) throws DockerException {
		WebResource webResource = client.resource(restEndpointUrl + "/images/search").queryParam("term", search);
		try {
			return webResource.accept(MediaType.APPLICATION_JSON).get(new GenericType<List<SearchItem>>() {
			});
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error.", exception);
			} else {
				throw new DockerException(exception);
			}
		}

	}

	/**
	 * Remove an image, deleting any tags it might have.
	 */
	public void removeImage(String imageId) throws DockerException {
		Preconditions.checkState(!StringUtils.isEmpty(imageId), "Image ID can't be empty");

		try {
			WebResource webResource = client.resource(restEndpointUrl + "/images/" + imageId)
					.queryParam("force", "true");
			LOGGER.trace("DELETE: {}", webResource);
			webResource.delete();
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 204) {
				//no error
				LOGGER.trace("Successfully removed image " + imageId);
			} else if (exception.getResponse().getStatus() == 404) {
				LOGGER.warn("{} no such image", imageId);
			} else if (exception.getResponse().getStatus() == 409) {
				throw new DockerException("Conflict");
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error.", exception);
			} else {
				throw new DockerException(exception);
			}
		}

	}

	public void removeImages(List<String> images) throws DockerException {
		Preconditions.checkNotNull(images, "List of images can't be null");

		for (String imageId : images) {
			removeImage(imageId);
		}
	}

	public String getVizImages() throws DockerException {
		WebResource webResource = client.resource(restEndpointUrl + "/images/viz");

		try {
			LOGGER.trace("GET: {}", webResource);
			String response = webResource.get(String.class);
			LOGGER.trace("Response: {}", response);

			return response;
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 400) {
				throw new DockerException("bad parameter");
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}


	public List<Image> getImages() throws DockerException {
		return this.getImages(null, false);
	}

	public List<Image> getImages(boolean allContainers) throws DockerException {
		return this.getImages(null, allContainers);
	}

	public List<Image> getImages(String name) throws DockerException {
		return this.getImages(name, false);
	}

	public List<Image> getImages(String name, boolean allImages) throws DockerException {

		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("filter", name);
		params.add("all", allImages ? "1" : "0");

		WebResource webResource = client.resource(restEndpointUrl + "/images/json").queryParams(params);

		try {
			LOGGER.trace("GET: {}", webResource);
			List<Image> images = webResource.accept(MediaType.APPLICATION_JSON).get(new GenericType<List<Image>>() {
			});
			LOGGER.trace("Response: {}", images);
			return images;
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 400) {
				throw new DockerException("bad parameter");
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException();
			}
		}

	}

	public ImageInspectResponse inspectImage(String imageId) throws DockerException, NotFoundException {

		WebResource webResource = client.resource(restEndpointUrl + String.format("/images/%s/json", imageId));

		try {
			LOGGER.trace("GET: {}", webResource);
			return webResource.accept(MediaType.APPLICATION_JSON).get(ImageInspectResponse.class);
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 404) {
				throw new NotFoundException(String.format("No such image %s", imageId));
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}

	/**
	 * * CONTAINER API
	 * *
	 */

	public List<Container> listContainers(boolean allContainers) {
		return this.listContainers(allContainers, false, -1, false, null, null);
	}

	public List<Container> listContainers(boolean allContainers, boolean latest) {
		return this.listContainers(allContainers, latest, -1, false, null, null);
	}

	public List<Container> listContainers(boolean allContainers, boolean latest, int limit) {
		return this.listContainers(allContainers, latest, limit, false, null, null);
	}

	public List<Container> listContainers(boolean allContainers, boolean latest, int limit, boolean showSize) {
		return this.listContainers(allContainers, latest, limit, showSize, null, null);
	}

	public List<Container> listContainers(boolean allContainers, boolean latest, int limit, boolean showSize, String since) {
		return this.listContainers(allContainers, latest, limit, false, since, null);
	}

	public List<Container> listContainers(boolean allContainers, boolean latest, int limit, boolean showSize, String since, String before) {

		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("limit", latest ? "1" : String.valueOf(limit));
		params.add("all", allContainers ? "1" : "0");
		params.add("since", since);
		params.add("before", before);
		params.add("size", showSize ? "1" : "0");

		WebResource webResource = client.resource(restEndpointUrl + "/containers/json").queryParams(params);
		LOGGER.trace("GET: {}", webResource);
		List<Container> containers = webResource.accept(MediaType.APPLICATION_JSON).get(new GenericType<List<Container>>() {
		});
		LOGGER.trace("Response: {}", containers);

		return containers;
	}

	public ContainerCreateResponse createContainer(ContainerConfig config) throws DockerException {
		return createContainer(config, null);
	}

	public ContainerCreateResponse createContainer(ContainerConfig config, String name) throws DockerException, NotFoundException {

		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		if (name != null) {
			params.add("name", name);
		}
		WebResource webResource = client.resource(restEndpointUrl + "/containers/create").queryParams(params);

		try {
			LOGGER.trace("POST: {} ", webResource);
			return webResource.accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON)
					.post(ContainerCreateResponse.class, config);
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 404) {
				throw new NotFoundException(String.format("%s is an unrecognized image. Please pull the image first.", config.getImage()));
			} else if (exception.getResponse().getStatus() == 406) {
				throw new DockerException("impossible to attach (container not running)");
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}

	}

	public void startContainer(String containerId) throws DockerException {
		this.startContainer(containerId, null);
	}

	public void startContainer(String containerId, HostConfig hostConfig) throws DockerException, NotFoundException {

		WebResource webResource = client.resource(restEndpointUrl + String.format("/containers/%s/start", containerId));

		try {
			LOGGER.trace("POST: {}", webResource);
			Builder builder = webResource.accept(MediaType.TEXT_PLAIN);
			if (hostConfig != null) {
				builder.type(MediaType.APPLICATION_JSON).post(hostConfig);
			} else {
				builder.post((HostConfig) null);
			}
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 404) {
				throw new NotFoundException(String.format("No such container %s", containerId));
			} else if (exception.getResponse().getStatus() == 204) {
				//no error
				LOGGER.trace("Successfully started container {}", containerId);
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}

	public ContainerInspectResponse inspectContainer(String containerId) throws DockerException, NotFoundException {

		WebResource webResource = client.resource(restEndpointUrl + String.format("/containers/%s/json", containerId));

		try {
			LOGGER.trace("GET: {}", webResource);
			return webResource.accept(MediaType.APPLICATION_JSON).get(ContainerInspectResponse.class);
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 404) {
				throw new NotFoundException(String.format("No such container %s", containerId));
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}


	public void removeContainer(String container) throws DockerException {
		this.removeContainer(container, false);
	}

	public void removeContainer(String containerId, boolean removeVolumes) throws DockerException {
		Preconditions.checkState(!StringUtils.isEmpty(containerId), "Container ID can't be empty");

		WebResource webResource = client.resource(restEndpointUrl + "/containers/" + containerId).queryParam("v", removeVolumes ? "1" : "0");

		try {
			LOGGER.trace("DELETE: {}", webResource);
			String response = webResource.accept(MediaType.APPLICATION_JSON).delete(String.class);
			LOGGER.trace("Response: {}", response);
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 204) {
				//no error
				LOGGER.trace("Successfully removed container " + containerId);
			} else if (exception.getResponse().getStatus() == 400) {
				throw new DockerException("bad parameter");
			} else if (exception.getResponse().getStatus() == 404) {
				// should really throw a NotFoundException instead of silently ignoring the problem
				LOGGER.warn(String.format("%s is an unrecognized container.", containerId));
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}


	public void removeContainers(List<String> containers, boolean removeVolumes) throws DockerException {
		Preconditions.checkNotNull(containers, "List of containers can't be null");

		for (String containerId : containers) {
			removeContainer(containerId, removeVolumes);
		}
	}

	public int waitContainer(String containerId) throws DockerException, NotFoundException {
		WebResource webResource = client.resource(restEndpointUrl + String.format("/containers/%s/wait", containerId));

		try {
			LOGGER.trace("POST: {}", webResource);
			ObjectNode ObjectNode = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(ObjectNode.class);
            return ObjectNode.get("StatusCode").asInt();
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 404) {
				throw new NotFoundException(String.format("No such container %s", containerId));
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		} catch (Exception e) {
			throw new DockerException(e);
		}
	}


	public ClientResponse logContainer(String containerId) throws DockerException {
		return logContainer(containerId, false);
	}

	public ClientResponse logContainerStream(String containerId) throws DockerException {
		return logContainer(containerId, true);
	}

	private ClientResponse logContainer(String containerId, boolean stream) throws DockerException, NotFoundException {
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("logs", "1");
		params.add("stdout", "1");
		params.add("stderr", "1");
		if (stream) {
			params.add("stream", "1"); // this parameter keeps stream open indefinitely
		}

		WebResource webResource = client.resource(restEndpointUrl + String.format("/containers/%s/attach", containerId))
				.queryParams(params);

		try {
			LOGGER.trace("POST: {}", webResource);
			return webResource.accept(MediaType.APPLICATION_OCTET_STREAM_TYPE).post(ClientResponse.class, params);
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 400) {
				throw new DockerException("bad parameter");
			} else if (exception.getResponse().getStatus() == 404) {
				throw new NotFoundException(String.format("No such container %s", containerId));
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}

	public ClientResponse copyFile(String containerId, String resource) throws DockerException {
		CopyConfig copyConfig = new CopyConfig();
		copyConfig.setResource(resource);

		WebResource webResource =
				client.resource(restEndpointUrl + String.format("/containers/%s/copy", containerId));

		try {
			LOGGER.trace("POST: " + webResource.toString());
			WebResource.Builder builder =
					webResource.accept(MediaType.APPLICATION_OCTET_STREAM_TYPE).type("application/json");

			return builder.post(ClientResponse.class, copyConfig.toString());
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 400) {
				throw new DockerException("bad parameter");
			} else if (exception.getResponse().getStatus() == 404) {
				throw new DockerException(String.format("No such container %s", containerId));
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}

	public List<ChangeLog> containerDiff(String containerId) throws DockerException, NotFoundException {

		WebResource webResource = client.resource(restEndpointUrl + String.format("/containers/%s/changes", containerId));

		try {
			LOGGER.trace("GET: {}", webResource);
			return webResource.accept(MediaType.APPLICATION_JSON).get(new GenericType<List<ChangeLog>>() {
			});
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 404) {
				throw new NotFoundException(String.format("No such container %s", containerId));
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}


	public void stopContainer(String containerId) throws DockerException {
		this.stopContainer(containerId, 10);
	}

	public void stopContainer(String containerId, int timeout) throws DockerException {

		WebResource webResource = client.resource(restEndpointUrl + String.format("/containers/%s/stop", containerId))
				.queryParam("t", String.valueOf(timeout));


		try {
			LOGGER.trace("POST: {}", webResource);
			webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post();
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 404) {
				LOGGER.warn("No such container {}", containerId);
			} else if (exception.getResponse().getStatus() == 204) {
				//no error
				LOGGER.trace("Successfully stopped container {}", containerId);
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}

	public void kill(String containerId) throws DockerException {
		WebResource webResource = client.resource(restEndpointUrl + String.format("/containers/%s/kill", containerId));

		try {
			LOGGER.trace("POST: {}", webResource);
			webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post();
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 404) {
				LOGGER.warn("No such container {}", containerId);
			} else if (exception.getResponse().getStatus() == 204) {
				//no error
				LOGGER.trace("Successfully killed container {}", containerId);
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}

	public void restart(String containerId, int timeout) throws DockerException, NotFoundException {
		WebResource webResource = client.resource(restEndpointUrl + String.format("/containers/%s/restart", containerId));

		try {
			LOGGER.trace("POST: {}", webResource);
			webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post();
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 404) {
				throw new NotFoundException(String.format("No such container %s", containerId));
			} else if (exception.getResponse().getStatus() == 204) {
				//no error
				LOGGER.trace("Successfully restarted container {}", containerId);
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}

	public String commit(CommitConfig commitConfig) throws DockerException, NotFoundException {
		Preconditions.checkNotNull(commitConfig.getContainer(), "Container ID was not specified");

		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("container", commitConfig.getContainer());
		params.add("repo", commitConfig.getRepo());
		params.add("tag", commitConfig.getTag());
		params.add("m", commitConfig.getMessage());
		params.add("author", commitConfig.getAuthor());
		params.add("run", commitConfig.getRun());

		WebResource webResource = client.resource(restEndpointUrl + "/commit").queryParams(params);

		try {
			LOGGER.trace("POST: {}", webResource);
			ObjectNode ObjectNode = webResource.accept("application/vnd.docker.raw-stream").post(ObjectNode.class, params);
            return ObjectNode.get("Id").asText();
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 404) {
				throw new NotFoundException(String.format("No such container %s", commitConfig.getContainer()));
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		} catch (Exception e) {
			throw new DockerException(e);
		}
	}


	public ClientResponse build(File dockerFolder) throws DockerException {
		return this.build(dockerFolder, null);
	}

	public ClientResponse build(File dockerFolder, String tag) throws DockerException {
		return this.build(dockerFolder, tag, false);
	}
	
	private static boolean isFileResource(String resource)  {
        URI uri;
		try {
			uri = new URI(resource);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
        return uri.getScheme() == null || "file".equals(uri.getScheme());
    }

	public ClientResponse build(File dockerFolder, String tag, boolean noCache) throws DockerException {
		Preconditions.checkNotNull(dockerFolder, "Folder is null");
		Preconditions.checkArgument(dockerFolder.exists(), "Folder %s doesn't exist", dockerFolder);
		Preconditions.checkState(new File(dockerFolder, "Dockerfile").exists(), "Dockerfile doesn't exist in " + dockerFolder);

		// ARCHIVE TAR
		String archiveNameWithOutExtension = UUID.randomUUID().toString();

		File dockerFolderTar = null;

		try {
			File dockerFile = new File(dockerFolder, "Dockerfile");
			List<String> dockerFileContent = FileUtils.readLines(dockerFile);

			if (dockerFileContent.size() <= 0) {
				throw new DockerException(String.format("Dockerfile %s is empty", dockerFile));
			}

			List<File> filesToAdd = new ArrayList<File>();
			filesToAdd.add(dockerFile);

			for (String cmd : dockerFileContent) {
				if (StringUtils.startsWithIgnoreCase(cmd.trim(), "ADD")) {
					String addArgs[] = StringUtils.split(cmd, " \t");
					if (addArgs.length != 3) {
						throw new DockerException(String.format("Wrong format on line [%s]", cmd));
					}

					String resource = addArgs[1];
					
					if(isFileResource(resource)) {
						File src = new File(resource);
						if (!src.isAbsolute()) {
							src = new File(dockerFolder, resource).getCanonicalFile();
						} else {
							throw new DockerException(String.format("Source file %s must be relative to %s", src, dockerFolder));
						}

						if (!src.exists()) {
							throw new DockerException(String.format("Source file %s doesn't exist", src));
						}
						if (src.isDirectory()) {
							filesToAdd.addAll(FileUtils.listFiles(src, null, true));
						} else {
							filesToAdd.add(src);
						}
					} 
				}
			}

			dockerFolderTar = CompressArchiveUtil.archiveTARFiles(dockerFolder, filesToAdd, archiveNameWithOutExtension);

		} catch (IOException ex) {
			FileUtils.deleteQuietly(dockerFolderTar);
			throw new DockerException("Error occurred while preparing Docker context folder.", ex);
		}

		try {
			return build(FileUtils.openInputStream(dockerFolderTar), tag, noCache);
		} catch (IOException e) {
			throw new DockerException(e);
		} finally {
			FileUtils.deleteQuietly(dockerFolderTar);
		}
	}

	public ClientResponse build(InputStream tarStream, String tag, boolean noCache) throws DockerException {
		//We need to use Jersey HttpClient here, since ApacheHttpClient4 will not add boundary filed to
		//Content-Type: multipart/form-data; boundary=Boundary_1_372491238_1372806136625

		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("t", tag);
		if (noCache) {
			params.add("nocache", "true");
		}

		WebResource webResource = client.resource(restEndpointUrl + "/build").queryParams(params);

		try {
			LOGGER.trace("POST: {}", webResource);
			return webResource
					.type("application/tar")
					.accept(MediaType.TEXT_PLAIN)
					.post(ClientResponse.class, tarStream);
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}
}
