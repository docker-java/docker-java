package com.kpelykh.docker.client.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
public class Image {

    @JsonProperty("Repository")
    public String repository;

    @JsonProperty("Tag")
    public String tag;

    @JsonProperty("Id")
    public String id;

    @JsonProperty("Created")
    public long created;

    @JsonProperty("Size")
    public long size;

    @JsonProperty("VirtualSize")
    public long virtualSize;

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
