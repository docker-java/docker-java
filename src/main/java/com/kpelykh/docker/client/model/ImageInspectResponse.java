package com.kpelykh.docker.client.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
public class ImageInspectResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("parent") private String parent;

    @JsonProperty("created") private String created;

    @JsonProperty("container") private String container;

    @JsonProperty("container_config") private ContainerConfig containerConfig;

    @JsonProperty("Size") private int size;

    @JsonProperty("docker_version") private String dockerVersion;

    @JsonProperty("config") private ContainerConfig config;

    @JsonProperty("architecture") private String arch;

    @JsonProperty("comment") private String comment;

    @JsonProperty("author") private String author;

    public String getId() {
        return id;
    }

    public String getParent() {
        return parent;
    }

    public String getCreated() {
        return created;
    }

    public String getContainer() {
        return container;
    }

    public ContainerConfig getContainerConfig() {
        return containerConfig;
    }

    public int getSize() {
        return size;
    }

    public String getDockerVersion() {
        return dockerVersion;
    }

    public ContainerConfig getConfig() {
        return config;
    }

    public String getArch() {
        return arch;
    }

    public String getComment() {
        return comment;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "ImageInspectResponse{" +
                "id='" + id + '\'' +
                ", parent='" + parent + '\'' +
                ", created='" + created + '\'' +
                ", container='" + container + '\'' +
                ", containerConfig=" + containerConfig +
                ", size=" + size +
                ", dockerVersion='" + dockerVersion + '\'' +
                ", config=" + config +
                ", arch='" + arch + '\'' +
                ", comment='" + comment + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
