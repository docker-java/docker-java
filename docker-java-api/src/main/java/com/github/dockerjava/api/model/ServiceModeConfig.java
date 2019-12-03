package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
public class ServiceModeConfig implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Replicated")
    private ServiceReplicatedModeOptions replicated;

    /**
     * @since 1.24
     */
    @JsonProperty("Global")
    private ServiceGlobalModeOptions global;

    /**
     * @since 1.24
     */
    @CheckForNull
    public ServiceMode getMode() {
        if (replicated != null) {
            return ServiceMode.REPLICATED;
        }
        if (global != null) {
            return ServiceMode.GLOBAL;
        }

        return null;
    }

    /**
     * @since 1.24
     */
    @CheckForNull
    public ServiceReplicatedModeOptions getReplicated() {
        return replicated;
    }

    /**
     * @since 1.24
     */
    public ServiceModeConfig withReplicated(ServiceReplicatedModeOptions replicated) {
        if (replicated != null && this.global != null) {
            throw new IllegalStateException("Cannot set both replicated and global mode");
        }
        this.replicated = replicated;

        return this;
    }

    /**
     * @since 1.24
     */
    @CheckForNull
    public ServiceGlobalModeOptions getGlobal() {
        return global;
    }

    /**
     * @since 1.24
     */
    public ServiceModeConfig withGlobal(ServiceGlobalModeOptions global) {
        if (global != null && this.replicated != null) {
            throw new IllegalStateException("Cannot set both global and replicated mode");
        }
        this.global = global;

        return this;
    }
}
