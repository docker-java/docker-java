package com.kpelykh.docker.client;

import com.google.common.base.Preconditions;
import com.kpelykh.docker.client.model.*;
import com.kpelykh.docker.client.utils.JsonClientFilter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.InputStream;
import java.util.List;

/**
 *
  * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
public class DockerClient
{

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerClient.class);

    private static DockerClient instance;
    private Client client;
    private String restEndpointUrl;

    public static DockerClient getInstance(String serverUrl) {
        if (instance == null) {
            instance = new DockerClient(serverUrl);
        }
        return instance;
    }

    private DockerClient(String serverUrl) {
        restEndpointUrl = serverUrl;
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        //clientConfig.getClasses().add(DockerRawStreamProvider.class);
        client = ApacheHttpClient4.create(clientConfig);
        client.addFilter(new JsonClientFilter());
    }


    /**
     ** MISC API
     **
     **/

    public Info info() throws DockerClientException {
        WebResource webResource = client.resource(restEndpointUrl + "/info");

        try {
            LOGGER.trace("GET: " + webResource.toString());
            return webResource.accept(MediaType.APPLICATION_JSON).get(Info.class);
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 500) {
                throw new DockerClientException("Server error.", exception);
            } else {
                throw new DockerClientException(exception);
            }
        }
    }


    public Version version() throws DockerClientException {
        WebResource webResource = client.resource(restEndpointUrl + "/version");

        try {
            LOGGER.trace("GET: " + webResource.toString());
            return webResource.accept(MediaType.APPLICATION_JSON).get(Version.class);
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 500) {
                throw new DockerClientException("Server error.", exception);
            } else {
                throw new DockerClientException(exception);
            }
        }
    }


    /**
     ** IMAGE API
     **
     **/

    public InputStream pull(String repository) throws DockerClientException {
        return this.pull(repository, null, null);
    }

    public InputStream pull(String repository, String tag) throws DockerClientException {
        return this.pull(repository, tag, null);
    }

    public InputStream pull(String repository, String tag, String registry) throws DockerClientException {
        Preconditions.checkNotNull(repository, "Repository was not specified");

        if (StringUtils.countMatches(repository, ":") == 1) {
            String repositoryTag[] = StringUtils.split(repository);
            repository = repositoryTag[0];
            tag = repositoryTag[1];

        }

        MultivaluedMap<String,String> params = new MultivaluedMapImpl();
        params.add("tag", tag);
        params.add("fromImage", repository);
        params.add("registry", registry);

        WebResource webResource = client.resource(restEndpointUrl + "/images/create").queryParams(params);

        try {
            LOGGER.trace("POST: " + webResource.toString());
            return webResource.accept(MediaType.APPLICATION_JSON).post(InputStream.class);
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 500) {
                throw new DockerClientException("Server error.", exception);
            } else {
                throw new DockerClientException(exception);
            }
        }
    }

    public List<SearchItem> search(String search) throws DockerClientException {
        WebResource webResource = client.resource(restEndpointUrl + "/images/search").queryParam("term", search);
        try {
            return webResource.accept(MediaType.APPLICATION_JSON).get(new GenericType<List<SearchItem>>() {});
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 500) {
                throw new DockerClientException("Server error.", exception);
            } else {
                throw new DockerClientException(exception);
            }
        }

    }

    public void removeImage(String imageId) throws DockerClientException {
        Preconditions.checkState(!StringUtils.isEmpty(imageId), "Image ID can't be empty");

        try {
            WebResource webResource = client.resource(restEndpointUrl + "/images/" + imageId);
            LOGGER.trace("DELETE: " + webResource.toString());
            webResource.delete();
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 204) {
                //no error
                LOGGER.trace("Successfully removed image " + imageId);
            } else if (exception.getResponse().getStatus() == 404) {
                LOGGER.warn(String.format("%s no such image", imageId));
            } else if (exception.getResponse().getStatus() == 409) {
                throw new DockerClientException("Conflict");
            } else if (exception.getResponse().getStatus() == 500) {
                throw new DockerClientException("Server error.", exception);
            } else {
                throw new DockerClientException(exception);
            }
        }

    }

    public void removeImages(List<Image> images) throws DockerClientException {
        Preconditions.checkNotNull(images, "List of images can't be null");

        for (Image image : images) {
            removeImage(image.id);
        }
    }

    public String getVizImages() throws DockerClientException {
        WebResource webResource = client.resource(restEndpointUrl + "/images/viz");

        try {
            LOGGER.trace("GET: " + webResource.toString());
            String response = webResource.get(String.class);
            LOGGER.trace("Response:" + response.toString());

            return response;
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 400) {
                throw new DockerClientException("bad parameter");
            } else if (exception.getResponse().getStatus() == 500) {
                throw new DockerClientException("Server error", exception);
            } else {
                throw new DockerClientException(exception);
            }
        }
    }


    public List<Image> getImages() throws DockerClientException {
        return this.getImages(null, false);
    }

    public List<Image> getImages(boolean allContainers) throws DockerClientException {
        return this.getImages(null, allContainers);
    }

    public List<Image> getImages(String name) throws DockerClientException {
        return this.getImages(name, false);
    }

    public List<Image> getImages(String name, boolean allImages) throws DockerClientException {

        MultivaluedMap<String,String> params = new MultivaluedMapImpl();
        params.add("filter", name);
        params.add("all", allImages ? "1" : "0");

        WebResource webResource = client.resource(restEndpointUrl + "/images/json").queryParams(params);

        try {
            LOGGER.trace("GET: " + webResource.toString());
            List<Image> images = webResource.accept(MediaType.APPLICATION_JSON).get(new GenericType<List<Image>>() {});
            LOGGER.trace("Response:" + images.toString());
            return images;
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 400) {
                throw new DockerClientException("bad parameter");
            } else if (exception.getResponse().getStatus() == 500) {
                throw new DockerClientException("Server error", exception);
            } else {
                throw new DockerClientException();
            }
        }

    }

    public ImageInspectResponse inspectImage(String imageId) throws DockerClientException {

        WebResource webResource = client.resource(restEndpointUrl + String.format("/images/%s/json", imageId));

        try {
            LOGGER.trace("GET: " + webResource.toString());
            return webResource.accept(MediaType.APPLICATION_JSON).get(ImageInspectResponse.class);
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 404) {
                throw new DockerClientException(String.format("No such image %s", imageId));
            } else if (exception.getResponse().getStatus() == 500) {
                throw new DockerClientException("Server error", exception);
            } else {
                throw new DockerClientException(exception);
            }
        }
    }

    /**
     ** CONTAINER API
     **
     **/

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

        MultivaluedMap<String,String> params = new MultivaluedMapImpl();
        params.add("limit", latest ? "1" : String.valueOf(limit));
        params.add("all", allContainers ? "1" : "0");
        params.add("since", since);
        params.add("before", before);
        params.add("size", showSize ? "1" : "0");

        WebResource webResource = client.resource(restEndpointUrl + "/containers/json").queryParams(params);
        LOGGER.trace("GET: " + webResource.toString());
        List<Container> containers = webResource.accept(MediaType.APPLICATION_JSON).get(new GenericType<List<Container>>() {});
        LOGGER.trace("Response:" + containers.toString());

        return containers;
    }


    public ContainerCreateResponse createContainer(ContainerConfig config) throws DockerClientException {

        WebResource webResource = client.resource(restEndpointUrl + "/containers/create");

        try {
            LOGGER.trace("POST: " + webResource.toString());
            return webResource.accept(MediaType.APPLICATION_JSON)
                   .type(MediaType.APPLICATION_JSON)
                        .post(ContainerCreateResponse.class, config);
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 404) {
                throw new DockerClientException(String.format("%s is an unrecognized image. Please pull the image first.", config.image));
            } else if (exception.getResponse().getStatus() == 406) {
                throw new DockerClientException("impossible to attach (container not running)");
            } else if (exception.getResponse().getStatus() == 500) {
                throw new DockerClientException("Server error", exception);
            } else {
                throw new DockerClientException(exception);
            }
        }

    }

    public void startContainer(String containerId) throws DockerClientException {
        this.startContainer(containerId, null);
    }

    public void startContainer(String containerId, HostConfig hostConfig) throws DockerClientException {

        WebResource webResource = client.resource(restEndpointUrl + String.format("/containers/%s/start", containerId));

        try {
            LOGGER.trace("POST: " + webResource.toString());
            webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(hostConfig);
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 404) {
                throw new DockerClientException(String.format("No such container %s", containerId));
            } else if (exception.getResponse().getStatus() == 204) {
                //no error
                LOGGER.trace("Successfully started container " + containerId);
            } else if (exception.getResponse().getStatus() == 500) {
                throw new DockerClientException("Server error", exception);
            } else {
                throw new DockerClientException(exception);
            }
        }
    }

    public ContainerInspectResponse inspectContainer(String containerId) throws DockerClientException {

        WebResource webResource = client.resource(restEndpointUrl + String.format("/containers/%s/json", containerId));

        try {
            LOGGER.trace("GET: " + webResource.toString());
            return webResource.accept(MediaType.APPLICATION_JSON).get(ContainerInspectResponse.class);
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 404) {
                throw new DockerClientException(String.format("No such container %s", containerId));
            } else if (exception.getResponse().getStatus() == 500) {
                throw new DockerClientException("Server error", exception);
            } else {
                throw new DockerClientException(exception);
            }
        }
    }


    public void removeContainer(String container) throws DockerClientException {
        this.removeContainer(container, false);
    }

    public void removeContainer(String containerId, boolean removeVolumes) throws DockerClientException {
        Preconditions.checkState(!StringUtils.isEmpty(containerId), "Container ID can't be empty");

        WebResource webResource = client.resource(restEndpointUrl + "/containers/" + containerId).queryParam("v", removeVolumes ? "1" : "0");

        try {
            LOGGER.trace("DELETE: " + webResource.toString());
            String response = webResource.accept(MediaType.APPLICATION_JSON).delete(String.class);
            LOGGER.trace("Response:" + response);
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 204) {
                //no error
                LOGGER.trace("Successfully removed container " + containerId);
            } else if (exception.getResponse().getStatus() == 400) {
                throw new DockerClientException("bad parameter");
            } else if (exception.getResponse().getStatus() == 404) {
                LOGGER.warn(String.format("%s is an unrecognized container.", containerId));
            } else if (exception.getResponse().getStatus() == 500) {
                throw new DockerClientException("Server error", exception);
            } else {
                throw new DockerClientException(exception);
            }
        }
    }


    public void removeContainers(List<Container> containers, boolean removeVolumes) throws DockerClientException {
        Preconditions.checkNotNull(containers, "List of containers can't be null");

        for (Container container : containers) {
            removeContainer(container.id, removeVolumes);
        }

    }

    public int waitContainer(String containerId) throws DockerClientException {
        WebResource webResource = client.resource(restEndpointUrl + String.format("/containers/%s/wait", containerId));

        try {
            LOGGER.trace("POST: " + webResource.toString());
            JSONObject jsonObject = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(JSONObject.class);
            return jsonObject.getInt("StatusCode");
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 404) {
                throw new DockerClientException(String.format("No such container %s", containerId));
            } else if (exception.getResponse().getStatus() == 500) {
                throw new DockerClientException("Server error", exception);
            } else {
                throw new DockerClientException(exception);
            }
        } catch (JSONException e) {
            throw new DockerClientException(e);
        }
    }


    public String logContainer(String containerId) throws DockerClientException {
        MultivaluedMap<String,String> params = new MultivaluedMapImpl();
        params.add("logs", "1");
        params.add("stdout", "1");
        params.add("stderr", "1");

        WebResource webResource = client.resource(restEndpointUrl + String.format("/containers/%s/attach", containerId))
                .queryParams(params);

        try {
            LOGGER.trace("POST: " + webResource.toString());
            return webResource.accept("application/vnd.docker.raw-stream").post(String.class, params);
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 400) {
                throw new DockerClientException("bad parameter");
            } else if (exception.getResponse().getStatus() == 404) {
                throw new DockerClientException(String.format("No such container %s", containerId));
            } else if (exception.getResponse().getStatus() == 500) {
                throw new DockerClientException("Server error", exception);
            } else {
                throw new DockerClientException(exception);
            }
        }
    }

    public List<ChangeLog> containterDiff(String containerId) throws DockerClientException {

        WebResource webResource = client.resource(restEndpointUrl + String.format("/containers/%s/changes", containerId));

        try {
            LOGGER.trace("GET: " + webResource.toString());
            return webResource.accept(MediaType.APPLICATION_JSON).get(new GenericType<List<ChangeLog>>() {});
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 404) {
                throw new DockerClientException(String.format("No such container %s", containerId));
            } else if (exception.getResponse().getStatus() == 500) {
                throw new DockerClientException("Server error", exception);
            } else {
                throw new DockerClientException(exception);
            }
        }
    }




    public void stopContainer(String containerId) throws DockerClientException {
        this.stopContainer(containerId, 10);
    }

    public void stopContainer(String containerId, int timeout) throws DockerClientException {

        WebResource webResource = client.resource(restEndpointUrl + String.format("/containers/%s/stop", containerId))
                .queryParam("t", String.valueOf(timeout));


        try {
            LOGGER.trace("POST: " + webResource.toString());
            webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post();
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 404) {
                LOGGER.warn(String.format("No such container %s", containerId));
            } else if (exception.getResponse().getStatus() == 204) {
                //no error
                LOGGER.trace("Successfully stopped container " + containerId);
            } else if (exception.getResponse().getStatus() == 500) {
                throw new DockerClientException("Server error", exception);
            } else {
                throw new DockerClientException(exception);
            }
        }
    }

    public void kill(String containerId) throws DockerClientException {
        WebResource webResource = client.resource(restEndpointUrl + String.format("/containers/%s/kill", containerId));

        try {
            LOGGER.trace("POST: " + webResource.toString());
            webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post();
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 404) {
                LOGGER.warn(String.format("No such container %s", containerId));
            } else if (exception.getResponse().getStatus() == 204) {
                //no error
                LOGGER.trace("Successfully killed container " + containerId);
            } else if (exception.getResponse().getStatus() == 500) {
                throw new DockerClientException("Server error", exception);
            } else {
                throw new DockerClientException(exception);
            }
        }
    }

    public void restart(String containerId, int timeout) throws DockerClientException {
        WebResource webResource = client.resource(restEndpointUrl + String.format("/containers/%s/restart", containerId));

        try {
            LOGGER.trace("POST: " + webResource.toString());
            webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post();
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 404) {
                throw new DockerClientException(String.format("No such container %s", containerId));
            } else if (exception.getResponse().getStatus() == 204) {
                //no error
                LOGGER.trace("Successfully restarted container " + containerId);
            } else if (exception.getResponse().getStatus() == 500) {
                throw new DockerClientException("Server error", exception);
            } else {
                throw new DockerClientException(exception);
            }
        }
    }

    public String commit(CommitConfig commitConfig) throws DockerClientException {
        Preconditions.checkNotNull(commitConfig.container, "Container ID was not specified");

        MultivaluedMap<String,String> params = new MultivaluedMapImpl();
        params.add("container", commitConfig.container);
        params.add("repo", commitConfig.repo);
        params.add("tag", commitConfig.tag);
        params.add("m", commitConfig.message);
        params.add("author", commitConfig.author);
        params.add("run", commitConfig.run);

        WebResource webResource = client.resource(restEndpointUrl + "/commit").queryParams(params);

        try {
            LOGGER.trace("POST: " + webResource.toString());
            JSONObject jsonObject = webResource.accept("application/vnd.docker.raw-stream").post(JSONObject.class, params);
            return jsonObject.getString("Id");
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 404) {
                throw new DockerClientException(String.format("No such container %s", commitConfig.container));
            } else if (exception.getResponse().getStatus() == 500) {
                throw new DockerClientException("Server error", exception);
            } else {
                throw new DockerClientException(exception);
            }
        } catch (JSONException e) {
            throw new DockerClientException(e);
        }
    }


}
