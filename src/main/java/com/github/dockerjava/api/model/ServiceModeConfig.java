package com.github.dockerjava.api.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * For ServiceSpec
 */
public class ServiceModeConfig {

    private ServiceReplicatedModeOptions replicated;

    private ServiceGlobalModeOptions global;

    public ServiceMode getMode() {
        if (replicated != null) {
            return ServiceMode.REPLICATED;
        }
        if (global != null) {
            return ServiceMode.GLOBAL;
        }

        return null;
    }

    public ServiceReplicatedModeOptions getReplicated() {
        return replicated;
    }

    public ServiceModeConfig withReplicated(ServiceReplicatedModeOptions replicated) {
        if (replicated != null && this.global != null) {
            throw new IllegalStateException("Cannot set both replicated and global mode");
        }
        this.replicated = replicated;

        return this;
    }

    public ServiceGlobalModeOptions getGlobal() {
        return global;
    }

    public ServiceModeConfig withGlobal(ServiceGlobalModeOptions global) {
        if (global != null && this.replicated != null) {
            throw new IllegalStateException("Cannot set both global and replicated mode");
        }
        this.global = global;

        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
