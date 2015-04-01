package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.model.ContainerConfig;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InspectImageResponse {

	@JsonProperty("Architecture")
    private String arch;

	@JsonProperty("Author")
    private String author;

	@JsonProperty("Comment")
    private String comment;

	@JsonProperty("Config")
    private ContainerConfig config;

	@JsonProperty("Container")
    private String container;

	@JsonProperty("ContainerConfig")
    private ContainerConfig containerConfig;

	@JsonProperty("Created")
	private String created;

	@JsonProperty("DockerVersion")
    private String dockerVersion;

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Os")
    private String os;

    @JsonProperty("Parent")
    private String parent;

    @JsonProperty("Size")
    private long size;

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

    public long getSize() {
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

    public String getOs() {
        return os;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
