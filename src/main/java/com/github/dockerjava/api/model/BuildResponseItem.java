package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a build response stream item
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public class BuildResponseItem extends ResponseItem {

    private static final long serialVersionUID = -1252904184236343612L;

    private static final String BUILD_SUCCESS = "Successfully built";

    @JsonProperty("stream")
    private String stream;

    public String getStream() {
        return stream;
    }

    /**
     * Returns whether the stream field indicates a successful build operation
     */
    @JsonIgnore
    public Boolean isBuildSuccessIndicated() {
        if (getStream() == null)
            return false;

        return getStream().contains(BUILD_SUCCESS);
    }

    @JsonIgnore
    public String getImageId() {
        if (!isBuildSuccessIndicated())
            return null;

        return getStream().replaceFirst(BUILD_SUCCESS, "").trim();
    }
}
