package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a build response stream item
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public class BuildResponseItem extends ResponseItem {

    private static final long serialVersionUID = -1252904184236343612L;

    @JsonProperty("stream")
    private String stream;

    public String getStream() {
        return stream;
    }
}
