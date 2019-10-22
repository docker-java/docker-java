package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceRequirements implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Limits")
    private ResourceSpecs limits;

    /**
     * @since 1.24
     */
    @JsonProperty("Reservations")
    private ResourceSpecs reservations;

    /**
     * @see #limits
     */
    @CheckForNull
    public ResourceSpecs getLimits() {
        return limits;
    }

    /**
     * @see #limits
     */
    public ResourceRequirements withLimits(ResourceSpecs limits) {
        this.limits = limits;
        return this;
    }

    /**
     * @see #reservations
     */
    @CheckForNull
    public ResourceSpecs getReservations() {
        return reservations;
    }

    /**
     * @see #reservations
     */
    public ResourceRequirements withReservations(ResourceSpecs reservations) {
        this.reservations = reservations;
        return this;
    }

    @Override
    public String toString() {
        return "ResourceRequirements{" +
                "limits=" + limits +
                ", reservations=" + reservations +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceRequirements that = (ResourceRequirements) o;
        return Objects.equals(limits, that.limits) &&
                Objects.equals(reservations, that.reservations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(limits, reservations);
    }
}
