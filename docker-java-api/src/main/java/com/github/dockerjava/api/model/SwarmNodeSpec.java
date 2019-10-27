package com.github.dockerjava.api.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    @Override
    public String toString() {
        return "SwarmNodeSpec{" +
                "name='" + name + '\'' +
                ", role=" + role +
                ", availability=" + availability +
                ", labels=" + labels +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SwarmNodeSpec that = (SwarmNodeSpec) o;
        return Objects.equals(name, that.name) &&
                role == that.role &&
                availability == that.availability &&
                Objects.equals(labels, that.labels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, role, availability, labels);
    }
}
