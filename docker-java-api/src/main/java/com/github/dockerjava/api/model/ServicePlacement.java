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
     * @since 1.40
     */
    @JsonProperty("MaxReplicas")
    private Integer maxReplicas;

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

    /**
     * Specifies the maximum amount of replicas / tasks that can run on one node.
     * 0 means unlimited replicas per node.
     *
     * @param maxReplicas Max number of replicas
     * @return This instance of ServicePlacement
     * @throws IllegalArgumentException if maxReplicas is less than 0
     */
    public ServicePlacement withMaxReplicas(int maxReplicas) {
        if (maxReplicas < 0) {
            throw new IllegalArgumentException("The Value for MaxReplicas must be greater or equal to 0");
        }

        this.maxReplicas = maxReplicas;
        return this;
    }

    /**
     * Getter for maxReplicas
     *
     * @return The maximum amount of replicas / tasks that can run on one node.
     */
    public Integer getMaxReplicas() {
        return this.maxReplicas;
    }

}
