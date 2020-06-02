package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.exception.DockerClientException;
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
    private int maxReplicas = 0;

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
     */
    public ServicePlacement withMaxReplicas(int maxReplicas) throws DockerClientException {
        if (maxReplicas >= 0) {
            this.maxReplicas = maxReplicas;

        } else {
            throw new IllegalArgumentException("The Value for MaxReplicas must be greater or equal to 0");
        }

        return this;
    }

    /**
     * Getter for maxReplicas
     *
     * @return The maximum amount of replicas / tasks that can run on one node.
     */
    public int getMaxReplicas() {
        return this.maxReplicas;
    }

}
