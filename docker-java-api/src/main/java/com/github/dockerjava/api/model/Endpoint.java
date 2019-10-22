package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public class Endpoint implements Serializable {
    public static final long serialVersionUID = 1L;

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
        return "Endpoint{" +
                "spec=" + spec +
                ", ports=" + Arrays.toString(ports) +
                ", virtualIPs=" + Arrays.toString(virtualIPs) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Endpoint endpoint = (Endpoint) o;
        return Objects.equals(spec, endpoint.spec) &&
                Arrays.equals(ports, endpoint.ports) &&
                Arrays.equals(virtualIPs, endpoint.virtualIPs);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(spec);
        result = 31 * result + Arrays.hashCode(ports);
        result = 31 * result + Arrays.hashCode(virtualIPs);
        return result;
    }
}
