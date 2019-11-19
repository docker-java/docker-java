package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
public class ServiceReplicatedModeOptions implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Replicas")
    private long replicas;

    /**
     * @see #replicas
     */
    public long getReplicas() {
        return replicas;
    }

    /**
     * @see #replicas
     */
    public ServiceReplicatedModeOptions withReplicas(int replicas) {
        this.replicas = replicas;
        return this;
    }
}
