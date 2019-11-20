package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents a build response stream item
 */
public class BuildResponseItem extends ResponseItem {
    private static final long serialVersionUID = -1252904184236343612L;

    private static final String BUILD_SUCCESS = "Successfully built";

    /**
     * Returns whether the stream field indicates a successful build operation
     */
    @JsonIgnore
    public boolean isBuildSuccessIndicated() {
        if (isErrorIndicated() || getStream() == null) {
            return false;
        }

        return getStream().contains(BUILD_SUCCESS);
    }

    @JsonIgnore
    public String getImageId() {
        if (!isBuildSuccessIndicated()) {
            return null;
        }

        return getStream().replaceFirst(BUILD_SUCCESS, "").trim();
    }
}
