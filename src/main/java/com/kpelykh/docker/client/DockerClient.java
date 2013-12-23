package com.kpelykh.docker.client;

import com.google.common.base.Preconditions;
import com.kpelykh.docker.client.model.*;
import com.kpelykh.docker.client.utils.CompressArchiveUtil;
import com.kpelykh.docker.client.utils.JsonClientFilter;
import com.sun.jersey.api.client.*;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.ApacheHttpClient4Handler;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

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

    public DockerClient(String serverUrl) {
        restEndpointUrl = "/v1.3";
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        //schemeRegistry.register(new Scheme("http", 4243, PlainSocketFactory.getSocketFactory()));

        PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
        // Increase max total connection
        cm.setMaxTotal(1000);
        // Increase default max connection per route
        cm.setDefaultMaxPerRoute(1000);

        //HttpClient httpClient = new DefaultHttpClient(cm);
        //client = new ApacheHttpClient4(new ApacheHttpClient4Handler(httpClient, null, false), clientConfig);
        client = new UnixSocketClient();

        //client.addFilter(new JsonClientFilter());
        //client.addFilter(new LoggingFilter());
    }

    /**
     ** MISC API
     **
     **/

    public Info info() throws DockerException {
        WebResource webResource = client.resource(restEndpointUrl + "/info");

        try {
            LOGGER.trace("GET: " + webResource.toString());
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
            LOGGER.trace("GET: " + webResource.toString());
            return webResource.accept(MediaType.APPLICATION_JSON).get(Version.class);
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 500) {
                throw new DockerException("Server error.", exception);
            } else {
                throw new DockerException(exception);
            }
        }
    }


    /**
     ** IMAGE API
     **
     **/

    public ClientResponse pull(String repository) throws DockerException {
        return this.pull(repository, null, null);
    }

    public ClientResponse pull(String repository, String tag) throws DockerException {
        return this.pull(repository, tag, null);
    }

    public ClientResponse pull(String repository, String tag, String registry) throws DockerException {
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
            return webResource.accept(MediaType.APPLICATION_OCTET_STREAM_TYPE).post(ClientResponse.class);
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
            return webResource.accept(MediaType.APPLICATION_JSON).get(new GenericType<List<SearchItem>>() {});
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 500) {
                throw new DockerException("Server error.", exception);
            } else {
                throw new DockerException(exception);
            }
        }

    }

    public void removeImage(String imageId) throws DockerException {
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
            LOGGER.trace("GET: " + webResource.toString());
            String response = webResource.get(String.class);
            LOGGER.trace("Response:" + response.toString());

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
                throw new DockerException("bad parameter");
            } else if (exception.getResponse().getStatus() == 500) {
                throw new DockerException("Server error", exception);
            } else {
                throw new DockerException();
            }
        }

    }

    public ImageInspectResponse inspectImage(String imageId) throws DockerException {

        WebResource webResource = client.resource(restEndpointUrl + String.format("/images/%s/json", imageId));

        try {
            LOGGER.trace("GET: " + webResource.toString());
            return webResource.accept(MediaType.APPLICATION_JSON).get(ImageInspectResponse.class);
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 404) {
                throw new DockerException(String.format("No such image %s", imageId));
            } else if (exception.getResponse().getStatus() == 500) {
                throw new DockerException("Server error", exception);
            } else {
                throw new DockerException(exception);
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


    public ContainerCreateResponse createContainer(ContainerConfig config) throws DockerException {

        WebResource webResource = client.resource(restEndpointUrl + "/containers/create");

        try {
            LOGGER.trace("POST: " + webResource.toString());
            return webResource.accept(MediaType.APPLICATION_JSON)
                   .type(MediaType.APPLICATION_JSON)
                        .post(ContainerCreateResponse.class, config);
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 404) {
                throw new DockerException(String.format("%s is an unrecognized image. Please pull the image first.", config.getImage()));
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

    public void startContainer(String containerId, HostConfig hostConfig) throws DockerException {

        WebResource webResource = client.resource(restEndpointUrl + String.format("/containers/%s/start", containerId));

        try {
            LOGGER.trace("POST: " + webResource.toString());
            Builder builder = webResource.accept(MediaType.TEXT_PLAIN);
            if (hostConfig != null) {
                builder.type(MediaType.APPLICATION_JSON).post(hostConfig);
            } else {
                builder.post((HostConfig) null);
            }
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 404) {
                throw new DockerException(String.format("No such container %s", containerId));
            } else if (exception.getResponse().getStatus() == 204) {
                //no error
                LOGGER.trace("Successfully started container " + containerId);
            } else if (exception.getResponse().getStatus() == 500) {
                throw new DockerException("Server error", exception);
            } else {
                throw new DockerException(exception);
            }
        }
    }

    public ContainerInspectResponse inspectContainer(String containerId) throws DockerException {

        WebResource webResource = client.resource(restEndpointUrl + String.format("/containers/%s/json", containerId));

        try {
            LOGGER.trace("GET: " + webResource.toString());
            return webResource.accept(MediaType.APPLICATION_JSON).get(ContainerInspectResponse.class);
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 404) {
                throw new DockerException(String.format("No such container %s", containerId));
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
            LOGGER.trace("DELETE: " + webResource.toString());
            String response = webResource.accept(MediaType.APPLICATION_JSON).delete(String.class);
            LOGGER.trace("Response:" + response);
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 204) {
                //no error
                LOGGER.trace("Successfully removed container " + containerId);
            } else if (exception.getResponse().getStatus() == 400) {
                throw new DockerException("bad parameter");
            } else if (exception.getResponse().getStatus() == 404) {
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

    public int waitContainer(String containerId) throws DockerException {
        WebResource webResource = client.resource(restEndpointUrl + String.format("/containers/%s/wait", containerId));

        try {
            LOGGER.trace("POST: " + webResource.toString());
            JSONObject jsonObject = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(JSONObject.class);
            return jsonObject.getInt("StatusCode");
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 404) {
                throw new DockerException(String.format("No such container %s", containerId));
            } else if (exception.getResponse().getStatus() == 500) {
                throw new DockerException("Server error", exception);
            } else {
                throw new DockerException(exception);
            }
        } catch (JSONException e) {
            throw new DockerException(e);
        }
    }


    public ClientResponse logContainer(String containerId) throws DockerException {
        return logContainer(containerId, false);
    }

    public ClientResponse logContainerStream(String containerId) throws DockerException {
        return logContainer(containerId, true);
    }

    private ClientResponse logContainer(String containerId, boolean stream) throws DockerException {
        MultivaluedMap<String,String> params = new MultivaluedMapImpl();
        params.add("logs", "1");
        params.add("stdout", "1");
        params.add("stderr", "1");
        if (stream) {
            params.add("stream", "1"); // this parameter keeps stream open indefinitely
        }

        WebResource webResource = client.resource(restEndpointUrl + String.format("/containers/%s/attach", containerId))
                .queryParams(params);

        try {
            LOGGER.trace("POST: " + webResource.toString());
            return webResource.accept(MediaType.APPLICATION_OCTET_STREAM_TYPE).post(ClientResponse.class, params);
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

    public List<ChangeLog> containterDiff(String containerId) throws DockerException {

        WebResource webResource = client.resource(restEndpointUrl + String.format("/containers/%s/changes", containerId));

        try {
            LOGGER.trace("GET: " + webResource.toString());
            return webResource.accept(MediaType.APPLICATION_JSON).get(new GenericType<List<ChangeLog>>() {});
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 404) {
                throw new DockerException(String.format("No such container %s", containerId));
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
            LOGGER.trace("POST: " + webResource.toString());
            webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post();
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 404) {
                LOGGER.warn(String.format("No such container %s", containerId));
            } else if (exception.getResponse().getStatus() == 204) {
                //no error
                LOGGER.trace("Successfully stopped container " + containerId);
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
            LOGGER.trace("POST: " + webResource.toString());
            webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post();
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 404) {
                LOGGER.warn(String.format("No such container %s", containerId));
            } else if (exception.getResponse().getStatus() == 204) {
                //no error
                LOGGER.trace("Successfully killed container " + containerId);
            } else if (exception.getResponse().getStatus() == 500) {
                throw new DockerException("Server error", exception);
            } else {
                throw new DockerException(exception);
            }
        }
    }

    public void restart(String containerId, int timeout) throws DockerException {
        WebResource webResource = client.resource(restEndpointUrl + String.format("/containers/%s/restart", containerId));

        try {
            LOGGER.trace("POST: " + webResource.toString());
            webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post();
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 404) {
                throw new DockerException(String.format("No such container %s", containerId));
            } else if (exception.getResponse().getStatus() == 204) {
                //no error
                LOGGER.trace("Successfully restarted container " + containerId);
            } else if (exception.getResponse().getStatus() == 500) {
                throw new DockerException("Server error", exception);
            } else {
                throw new DockerException(exception);
            }
        }
    }

    public String commit(CommitConfig commitConfig) throws DockerException {
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
                throw new DockerException(String.format("No such container %s", commitConfig.container));
            } else if (exception.getResponse().getStatus() == 500) {
                throw new DockerException("Server error", exception);
            } else {
                throw new DockerException(exception);
            }
        } catch (JSONException e) {
            throw new DockerException(e);
        }
    }


    public ClientResponse build(File dockerFolder) throws DockerException {
        return this.build(dockerFolder, null);
    }

    public ClientResponse build(File dockerFolder, String tag) throws DockerException {
        Preconditions.checkNotNull(dockerFolder, "Folder is null");
        Preconditions.checkArgument(dockerFolder.exists(), "Folder %s doesn't exist", dockerFolder);
        Preconditions.checkState(new File(dockerFolder, "Dockerfile").exists(), "Dockerfile doesn't exist in " + dockerFolder);

        //We need to use Jersey HttpClient here, since ApacheHttpClient4 will not add boundary filed to
        //Content-Type: multipart/form-data; boundary=Boundary_1_372491238_1372806136625

        MultivaluedMap<String,String> params = new MultivaluedMapImpl();
        params.add("t", tag);

        // ARCHIVE TAR
        String archiveNameWithOutExtension = UUID.randomUUID().toString();

        File dockerFolderTar = null;
        File tmpDockerContextFolder = null;

        try {
            File dockerFile = new File(dockerFolder, "Dockerfile");
            List<String> dockerFileContent = FileUtils.readLines(dockerFile);

            if (dockerFileContent.size() <= 0) {
                throw new DockerException(String.format("Dockerfile %s is empty", dockerFile));
            }

            //Create tmp docker context folder
            tmpDockerContextFolder = new File(FileUtils.getTempDirectoryPath(), "docker-java-build" + archiveNameWithOutExtension);

            FileUtils.copyFileToDirectory(dockerFile, tmpDockerContextFolder);

            for (String cmd : dockerFileContent) {
                if (StringUtils.startsWithIgnoreCase(cmd.trim(), "ADD")) {
                    String addArgs[] = StringUtils.split(cmd, " \t");
                    if (addArgs.length != 3) {
                        throw new DockerException(String.format("Wrong format on line [%s]", cmd));
                    }

                    File src = new File(addArgs[1]);
                    if (!src.isAbsolute()) {
                        src = new File(dockerFolder, addArgs[1]).getCanonicalFile();
                    }

                    if (!src.exists()) {
                        throw new DockerException(String.format("Sorce file %s doesnt' exist", src));
                    }
                    if (src.isDirectory()) {
                        FileUtils.copyDirectory(src, tmpDockerContextFolder);
                    } else {
                        FileUtils.copyFileToDirectory(src, tmpDockerContextFolder);
                    }
                }
            }

            dockerFolderTar = CompressArchiveUtil.archiveTARFiles(tmpDockerContextFolder, archiveNameWithOutExtension);

        } catch (IOException ex) {
            FileUtils.deleteQuietly(dockerFolderTar);
            FileUtils.deleteQuietly(tmpDockerContextFolder);
            throw new DockerException("Error occurred while preparing Docker context folder.", ex);
        }

        WebResource webResource = client.resource(restEndpointUrl + "/build").queryParams(params);

        try {
            LOGGER.trace("POST: " + webResource.toString());
            return webResource
                    .type("application/tar")
                    .accept(MediaType.TEXT_PLAIN)
                    .post(ClientResponse.class, FileUtils.openInputStream(dockerFolderTar));
        } catch (UniformInterfaceException exception) {
            if (exception.getResponse().getStatus() == 500) {
                throw new DockerException("Server error", exception);
            } else {
                throw new DockerException(exception);
            }
        } catch (IOException e) {
            throw new DockerException(e);
        } finally {
            FileUtils.deleteQuietly(dockerFolderTar);
            FileUtils.deleteQuietly(tmpDockerContextFolder);
        }

    }

}
