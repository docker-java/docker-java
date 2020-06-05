package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * Delete unused content (containers, images, volumes, networks, build relicts)
 */
@EqualsAndHashCode
@ToString
public class PruneResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("SpaceReclaimed")
    private Long spaceReclaimed;

    /**
     * Default constructor for the deserialization.
     */
    public PruneResponse() {
    }

    /**
     * Constructor.
     *
     * @param spaceReclaimed     Space reclaimed after purification
     */
    public PruneResponse(Long spaceReclaimed) {
        this.spaceReclaimed = spaceReclaimed;
    }

    /**
     * Disk space reclaimed in bytes
     */
    public Long getSpaceReclaimed() {
        return spaceReclaimed;
    }
}
