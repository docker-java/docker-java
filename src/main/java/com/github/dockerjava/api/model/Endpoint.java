package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Endpoint {

    @JsonProperty("Spec")
    private EndpointSpec spec;

    @JsonProperty("Ports")
    private PortConfig[] ports;

    @JsonProperty("VirtualIPs")
    private EndpointVirtualIP[] virtualIPs;

    public EndpointSpec getSpec() {
        return spec;
    }

    public Endpoint withSpec(EndpointSpec spec) {
        this.spec = spec;
        return this;
    }

    public PortConfig[] getPorts() {
        return ports;
    }

    public Endpoint withPorts(PortConfig[] ports) {
        this.ports = ports;
        return this;
    }

    public EndpointVirtualIP[] getVirtualIPs() {
        return virtualIPs;
    }

    public Endpoint withVirtualIPs(EndpointVirtualIP[] virtualIPs) {
        this.virtualIPs = virtualIPs;
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
