package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.Objects;

/**
 * Represents a build response stream item
 */
@EqualsAndHashCode
@ToString
public class BuildResponseItem extends ResponseItem {
    private static final long serialVersionUID = -1252904184236343612L;

    private static final String BUILD_SUCCESS = "Successfully built";
    private static final String SHA256 = "sha256:";

    /**
     * Returns whether the stream field indicates a successful build operation
     */
    @JsonIgnore
    public boolean isBuildSuccessIndicated() {
        // checking the new format
        if (Objects.equals(getId(), "moby.image.id")) {
            return true;
        }

        if (isErrorIndicated() || getStream() == null) {
            return false;
        }

        return getStream().contains(BUILD_SUCCESS) || getStream().startsWith(SHA256);
    }

    @JsonIgnore
    public String getImageId() {
        if (!isBuildSuccessIndicated()) {
            return null;
        }

        if (Objects.equals(getId(), "moby.image.id")) {
            return getAux().getId().replaceFirst(SHA256, "").trim();
        }

        if (getStream().startsWith(SHA256)) {
            return getStream().replaceFirst(SHA256, "").trim();
        }

        return getStream().replaceFirst(BUILD_SUCCESS, "").trim();
    }
}
