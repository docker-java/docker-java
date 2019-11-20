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
}
