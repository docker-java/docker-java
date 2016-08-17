package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * For TaskTemplate
 */
public class TaskResourcesSpec {

    @JsonProperty("Limits")
    private ResourceSpecs limits;

    @JsonProperty("Reservations")
    private ResourceSpecs reservations;

    public ResourceSpecs getLimits() {
        return limits;
    }

    public ResourceSpecs getReservations() {
        return reservations;
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
