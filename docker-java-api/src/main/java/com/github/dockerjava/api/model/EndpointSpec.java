package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    @Override
    public String toString() {
        return "EndpointSpec{" +
                "mode=" + mode +
                ", ports=" + ports +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EndpointSpec that = (EndpointSpec) o;
        return mode == that.mode &&
                Objects.equals(ports, that.ports);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mode, ports);
    }
}
