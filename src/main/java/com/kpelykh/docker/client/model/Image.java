package com.kpelykh.docker.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Image {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("RepoTags")
    private String[] repoTags;

    @JsonProperty("Repository")
    private String repository;

    @JsonProperty("Tag")
    private String tag;


    @JsonProperty("ParentId")
    private String parentId;

    @JsonProperty("Created")
    private long created;

    @JsonProperty("Size")
    private long size;

    @JsonProperty("VirtualSize")
    private long virtualSize;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getRepoTags() {
        return repoTags;
    }

    public void setRepoTags(String[] repoTags) {
        this.repoTags = repoTags;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getVirtualSize() {
        return virtualSize;
    }

    public void setVirtualSize(long virtualSize) {
        this.virtualSize = virtualSize;
    }

    @Override
    public String toString() {
        return "Image{" +
                "virtualSize=" + virtualSize +
                ", id='" + id + '\'' +
                ", repoTags=" + Arrays.toString(repoTags) +
                ", repository='" + repository + '\'' +
                ", tag='" + tag + '\'' +
                ", parentId='" + parentId + '\'' +
                ", created=" + created +
                ", size=" + size +
                '}';
    }
}
