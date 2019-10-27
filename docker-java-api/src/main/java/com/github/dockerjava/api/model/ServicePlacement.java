package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    @Override
    public String toString() {
        return "ServicePlacement{" +
                "constraints=" + constraints +
                ", platforms=" + platforms +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServicePlacement that = (ServicePlacement) o;
        return Objects.equals(constraints, that.constraints) &&
                Objects.equals(platforms, that.platforms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(constraints, platforms);
    }
}
