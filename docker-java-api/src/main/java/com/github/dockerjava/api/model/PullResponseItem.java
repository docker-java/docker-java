package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents a pull response stream item
 */
public class PullResponseItem extends ResponseItem {

    private static final long serialVersionUID = -2575482839766823293L;

    private static final String LEGACY_REGISTRY = "this image was pulled from a legacy registry";

    private static final String DOWNLOADED_NEWER_IMAGE = "Downloaded newer image";

    private static final String IMAGE_UP_TO_DATE = "Image is up to date";

    private static final String DOWNLOAD_COMPLETE = "Download complete";

    private static final String DOWNLOADED_SWARM = ": downloaded";

    /**
     * Returns whether the status indicates a successful pull operation
     *
     * @returns true: status indicates that pull was successful, false: status doesn't indicate a successful pull
     */
    @JsonIgnore
    public boolean isPullSuccessIndicated() {
        if (isErrorIndicated() || getStatus() == null) {
            return false;
        }

        return (getStatus().contains(DOWNLOAD_COMPLETE) ||
                getStatus().contains(IMAGE_UP_TO_DATE) ||
                getStatus().contains(DOWNLOADED_NEWER_IMAGE) ||
                getStatus().contains(LEGACY_REGISTRY) ||
                getStatus().contains(DOWNLOADED_SWARM)
        );
    }
}
