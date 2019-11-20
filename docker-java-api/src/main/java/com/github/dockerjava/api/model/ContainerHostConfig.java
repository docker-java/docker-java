package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * Used in {@link Container}
 *
 * @see Container
 * @author Kanstantsin Shautsou
 */
@EqualsAndHashCode
@ToString
public class ContainerHostConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("NetworkMode")
    private String networkMode;

    public String getNetworkMode() {
        return networkMode;
    }

    /**
     * @see #networkMode
     */
    public ContainerHostConfig withNetworkMode(String networkMode) {
        this.networkMode = networkMode;
        return this;
    }
}
