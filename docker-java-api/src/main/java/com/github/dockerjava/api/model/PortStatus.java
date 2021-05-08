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
public class PortStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Ports")
    private PortConfig[] ports;

    @CheckForNull
    public PortConfig[] getPorts() {
        return ports;
    }

    public PortStatus withPorts(PortConfig[] ports) {
        this.ports = ports;
        return this;
    }
}
