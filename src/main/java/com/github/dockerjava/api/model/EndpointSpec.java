package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class EndpointSpec {

    @JsonProperty("Mode")
    ResolutionMode mode;

    @JsonProperty("Ports")
    PortConfig[] ports;

    public ResolutionMode getMode() {
        return mode;
    }

    public EndpointSpec withMode(ResolutionMode mode) {
        this.mode = mode;
        return this;
    }

    public PortConfig[] getPorts() {
        return ports;
    }

    public EndpointSpec withPorts(PortConfig[] ports) {
        this.ports = ports;
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
