package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
public class Endpoint implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @FieldName("Spec")
    private EndpointSpec spec;

    /**
     * @since 1.24
     */
    @FieldName("Ports")
    private PortConfig[] ports;

    /**
     * @since 1.24
     */
    @FieldName("VirtualIPs")
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
}
