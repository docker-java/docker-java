package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
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

    @Override
    public String toString() {
        return "ServiceReplicatedModeOptions{" +
                "replicas=" + replicas +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceReplicatedModeOptions that = (ServiceReplicatedModeOptions) o;
        return replicas == that.replicas;
    }

    @Override
    public int hashCode() {
        return Objects.hash(replicas);
    }
}
