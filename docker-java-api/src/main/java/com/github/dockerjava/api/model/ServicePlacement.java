package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.List;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
public class ServicePlacement implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Constraints")
    private List<String> constraints;

    /**
     * @since 1.30
     */
    @JsonProperty("Platforms")
    private List<SwarmNodePlatform> platforms;

    /**
     * @see #constraints
     */
    @CheckForNull
    public List<String> getConstraints() {
        return constraints;
    }

    /**
     * @see #constraints
     */
    public ServicePlacement withConstraints(List<String> constraints) {
        this.constraints = constraints;
        return this;
    }

    /**
     * @see #platforms
     */
    public List<SwarmNodePlatform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<SwarmNodePlatform> platforms) {
        this.platforms = platforms;
    }
}
