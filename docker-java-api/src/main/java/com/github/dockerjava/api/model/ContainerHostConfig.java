package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * Used in {@link Container}
 *
 * @see Container
 * @author Kanstantsin Shautsou
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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

    @Override
    public String toString() {
        return "ContainerHostConfig{" +
                "networkMode='" + networkMode + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContainerHostConfig that = (ContainerHostConfig) o;
        return Objects.equals(networkMode, that.networkMode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(networkMode);
    }
}
