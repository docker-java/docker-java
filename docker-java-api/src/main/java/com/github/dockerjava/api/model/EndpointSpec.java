package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.List;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
public class EndpointSpec implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Mode")
    EndpointResolutionMode mode;

    /**
     * @since 1.24
     */
    @JsonProperty("Ports")
    List<PortConfig> ports;

    /**
     * @see #mode
     */
    @CheckForNull
    public EndpointResolutionMode getMode() {
        return mode;
    }

    /**
     * @see #mode
     */
    public EndpointSpec withMode(EndpointResolutionMode mode) {
        this.mode = mode;
        return this;
    }

    /**
     * @see #ports
     */
    @CheckForNull
    public List<PortConfig> getPorts() {
        return ports;
    }

    /**
     * @see #ports
     */
    public EndpointSpec withPorts(List<PortConfig> ports) {
        this.ports = ports;
        return this;
    }
}
