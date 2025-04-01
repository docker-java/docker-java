package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_48}
 */
@EqualsAndHashCode
@ToString
public class ImageOptions extends DockerObject implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("Subpath")
    private String subpath;

    public String getSubpath() {
        return subpath;
    }

    public ImageOptions withSubpath(String subpath) {
        this.subpath = subpath;
        return this;
    }
}
