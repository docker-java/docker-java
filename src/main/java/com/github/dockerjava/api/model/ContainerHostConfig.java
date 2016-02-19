package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @see Container
 * @author Kanstantsin Shautsou
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContainerHostConfig {
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
        return new ToStringBuilder(this)
                .append("networkMode", networkMode)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ContainerHostConfig that = (ContainerHostConfig) o;

        return new EqualsBuilder()
                .append(networkMode, that.networkMode)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(networkMode)
                .toHashCode();
    }
}
