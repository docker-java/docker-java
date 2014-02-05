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

    @JsonProperty("os") private String os;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public ContainerConfig getContainerConfig() {
        return containerConfig;
    }

    public void setContainerConfig(ContainerConfig containerConfig) {
        this.containerConfig = containerConfig;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getDockerVersion() {
        return dockerVersion;
    }

    public void setDockerVersion(String dockerVersion) {
        this.dockerVersion = dockerVersion;
    }

    public ContainerConfig getConfig() {
        return config;
    }

    public void setConfig(ContainerConfig config) {
        this.config = config;
    }

    public String getArch() {
        return arch;
    }

    public void setArch(String arch) {
        this.arch = arch;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
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
                ", os='" + os + '\'' +
                '}';
    }
}
