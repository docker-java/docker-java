package com.github.dockerjava.api.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Map;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
public class SwarmNodeSpec implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Name")
    private String name;

    /**
     * @since 1.24
     */
    @JsonProperty("Role")
    private SwarmNodeRole role;

    /**
     * @since 1.24
     */
    @JsonProperty("Availability")
    private SwarmNodeAvailability availability;

    /**
     * @since 1.24
     */
    @JsonProperty("Labels")
    public Map<String, String> labels;

    /**
     * @see #name
     */
    @CheckForNull
    public String getName() {
        return name;
    }

    /**
     * @see #name
     */
    public SwarmNodeSpec withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @see #role
     */
    @CheckForNull
    public SwarmNodeRole getRole() {
        return role;
    }

    /**
     * @see #role
     */
    public SwarmNodeSpec withRole(SwarmNodeRole role) {
        this.role = role;
        return this;
    }

    /**
     * @see #availability
     */
    @CheckForNull
    public SwarmNodeAvailability getAvailability() {
        return availability;
    }

    /**
     * @see #availability
     */
    public SwarmNodeSpec withAvailability(SwarmNodeAvailability availability) {
        this.availability = availability;
        return this;
    }

    /**
     * @see #labels
     */
    @CheckForNull
    public Map<String, String> getLabels() {
        return labels;
    }

    /**
     * @see #labels
     */
    public SwarmNodeSpec withLabels(Map<String, String> labels) {
        this.labels = labels;
        return this;
    }
}
