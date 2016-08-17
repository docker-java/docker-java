package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public class Endpoint implements Serializable {
    public static final Long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Spec")
    private EndpointSpec spec;

    /**
     * @since 1.24
     */
    @JsonProperty("Ports")
    private PortConfig[] ports;

    /**
     * @since 1.24
     */
    @JsonProperty("VirtualIPs")
    private EndpointVirtualIP[] virtualIPs;

    /**
     * @see #spec
     */
    @CheckForNull
    public EndpointSpec getSpec() {
        return spec;
    }

    /**
     * @see #spec
     */
    public Endpoint withSpec(EndpointSpec spec) {
        this.spec = spec;
        return this;
    }

    /**
     * @see #ports
     */
    @CheckForNull
    public PortConfig[] getPorts() {
        return ports;
    }

    /**
     * @see #ports
     */
    public Endpoint withPorts(PortConfig[] ports) {
        this.ports = ports;
        return this;
    }

    /**
     * @see #virtualIPs
     */
    @CheckForNull
    public EndpointVirtualIP[] getVirtualIPs() {
        return virtualIPs;
    }

    /**
     * @see #virtualIPs
     */
    public Endpoint withVirtualIPs(EndpointVirtualIP[] virtualIPs) {
        this.virtualIPs = virtualIPs;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
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
