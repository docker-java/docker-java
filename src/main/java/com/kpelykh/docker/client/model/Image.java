package com.kpelykh.docker.client.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
public class Image {

    @JsonProperty("Repository")
    private String repository;

    @JsonProperty("Tag")
    private String tag;

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Created")
    private long created;

    @JsonProperty("Size")
    private long size;

    @JsonProperty("VirtualSize")
    private long virtualSize;

    public String getRepository() {
        return repository;
    }

    public String getTag() {
        return tag;
    }

    public String getId() {
        return id;
    }

    public long getCreated() {
        return created;
    }

    public long getSize() {
        return size;
    }

    public long getVirtualSize() {
        return virtualSize;
    }

    @Override
    public String toString() {
        return "Image{" +
                "repository='" + repository + '\'' +
                ", tag='" + tag + '\'' +
                ", id='" + id + '\'' +
                ", created=" + created +
                ", size=" + size +
                ", virtualSize=" + virtualSize +
                '}';
    }

}
