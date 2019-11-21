package com.github.dockerjava.api.command;

import com.github.dockerjava.api.annotation.FieldName;
import com.github.dockerjava.api.model.ContainerConfig;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.util.List;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@EqualsAndHashCode
@ToString
public class InspectImageResponse {

    @FieldName("Architecture")
    private String arch;

    @FieldName("Author")
    private String author;

    @FieldName("Comment")
    private String comment;

    @FieldName("Config")
    private ContainerConfig config;

    @FieldName("Container")
    private String container;

    @FieldName("ContainerConfig")
    private ContainerConfig containerConfig;

    @FieldName("Created")
    private String created;

    @FieldName("DockerVersion")
    private String dockerVersion;

    @FieldName("Id")
    private String id;

    @FieldName("Os")
    private String os;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_25}
     */
    @FieldName("OsVersion")
    private String osVersion;

    @FieldName("Parent")
    private String parent;

    @FieldName("Size")
    private Long size;

    @FieldName("RepoTags")
    private List<String> repoTags;

    @FieldName("RepoDigests")
    private List<String> repoDigests;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_21}
     */
    @FieldName("VirtualSize")
    private Long virtualSize;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_21}
     */
    @FieldName("GraphDriver")
    private GraphDriver graphDriver;

    @FieldName("RootFS")
    private RootFS rootFS;

    /**
     * @see #arch
     */
    @CheckForNull
    public String getArch() {
        return arch;
    }

    /**
     * @see #arch
     */
    public InspectImageResponse withArch(String arch) {
        this.arch = arch;
        return this;
    }

    /**
     * @see #author
     */
    @CheckForNull
    public String getAuthor() {
        return author;
    }

    /**
     * @see #author
     */
    public InspectImageResponse withAuthor(String author) {
        this.author = author;
        return this;
    }

    /**
     * @see #comment
     */
    @CheckForNull
    public String getComment() {
        return comment;
    }

    /**
     * @see #comment
     */
    public InspectImageResponse withComment(String comment) {
        this.comment = comment;
        return this;
    }

    /**
     * Get the image commit configuration
     * @see #config
     */
    @CheckForNull
    public ContainerConfig getConfig() {
        return config;
    }

    /**
     * @see #config
     */
    public InspectImageResponse withConfig(ContainerConfig config) {
        this.config = config;
        return this;
    }

    /**
     * @see #container
     */
    @CheckForNull
    public String getContainer() {
        return container;
    }

    /**
     * @see #container
     */
    public InspectImageResponse withContainer(String container) {
        this.container = container;
        return this;
    }

    /**
     * If the image was created from a container, this config contains the configuration of the container
     * which was committed in this image
     * @see #containerConfig
     */
    @CheckForNull
    public ContainerConfig getContainerConfig() {
        return containerConfig;
    }

    /**
     * @see #containerConfig
     */
    public InspectImageResponse withContainerConfig(ContainerConfig containerConfig) {
        this.containerConfig = containerConfig;
        return this;
    }

    /**
     * @see #created
     */
    @CheckForNull
    public String getCreated() {
        return created;
    }

    /**
     * @see #created
     */
    public InspectImageResponse withCreated(String created) {
        this.created = created;
        return this;
    }

    /**
     * @see #dockerVersion
     */
    @CheckForNull
    public String getDockerVersion() {
        return dockerVersion;
    }

    /**
     * @see #dockerVersion
     */
    public InspectImageResponse withDockerVersion(String dockerVersion) {
        this.dockerVersion = dockerVersion;
        return this;
    }

    /**
     * @see #id
     */
    @CheckForNull
    public String getId() {
        return id;
    }

    /**
     * @see #id
     */
    public InspectImageResponse withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * @see #os
     */
    @CheckForNull
    public String getOs() {
        return os;
    }

    /**
     * @see #os
     */
    public InspectImageResponse withOs(String os) {
        this.os = os;
        return this;
    }

    /**
     * @see #osVersion
     */
    @CheckForNull
    public String getOsVersion() {
        return osVersion;
    }

    /**
     * @see #osVersion
     */
    public InspectImageResponse withOsVersion(String osVersion) {
        this.osVersion = osVersion;
        return this;
    }

    /**
     * @see #parent
     */
    @CheckForNull
    public String getParent() {
        return parent;
    }

    /**
     * @see #parent
     */
    public InspectImageResponse withParent(String parent) {
        this.parent = parent;
        return this;
    }

    /**
     * @see #repoTags
     */
    @CheckForNull
    public List<String> getRepoTags() {
        return repoTags;
    }

    /**
     * @see #repoTags
     */
    public InspectImageResponse withRepoTags(List<String> repoTags) {
        this.repoTags = repoTags;
        return this;
    }

    /**
     * @see #size
     */
    @CheckForNull
    public Long getSize() {
        return size;
    }

    /**
     * @see #size
     */
    public InspectImageResponse withSize(Long size) {
        this.size = size;
        return this;
    }

    /**
     * @see #repoDigests
     */
    @CheckForNull
    public List<String> getRepoDigests() {
        return repoDigests;
    }

    /**
     * @see #repoDigests
     */
    public InspectImageResponse withRepoDigests(List<String> repoDigests) {
        this.repoDigests = repoDigests;
        return this;
    }

    /**
     * @see #graphDriver
     */
    @CheckForNull
    public GraphDriver getGraphDriver() {
        return graphDriver;
    }

    /**
     * @see #graphDriver
     */
    public InspectImageResponse withGraphDriver(GraphDriver graphDriver) {
        this.graphDriver = graphDriver;
        return this;
    }

    /**
     * @see #virtualSize
     */
    @CheckForNull
    public Long getVirtualSize() {
        return virtualSize;
    }

    /**
     * @see #virtualSize
     */
    public InspectImageResponse withVirtualSize(Long virtualSize) {
        this.virtualSize = virtualSize;
        return this;
    }

    /**
     * @see #rootFS
     */
    @CheckForNull
    public RootFS getRootFS() {
        return rootFS;
    }

    /**
     * @see #rootFS
     */
    public InspectImageResponse withRootFS(RootFS rootFS) {
        this.rootFS = rootFS;
        return this;
    }
}
