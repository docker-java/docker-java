package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * Delete unused content (containers, images, volumes, networks, build relicts)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PruneResponse {");
        sb.append(" spaceReclaimed: ").append(getSpaceReclaimed());
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PruneResponse that = (PruneResponse) o;
        return Objects.equals(spaceReclaimed, that.spaceReclaimed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(spaceReclaimed);
    }
}
