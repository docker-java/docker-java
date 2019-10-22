package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public class EndpointVirtualIP implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("NetworkID")
    private String networkID;

    /**
     * @since 1.24
     */
    @JsonProperty("Addr")
    private String addr;

    /**
     * @see #networkID
     */
    @CheckForNull
    public String getNetworkID() {
        return networkID;
    }

    /**
     * @see #networkID
     */
    public EndpointVirtualIP withNetworkID(String networkID) {
        this.networkID = networkID;
        return this;
    }

    /**
     * @see #addr
     */
    @CheckForNull
    public String getAddr() {
        return addr;
    }

    /**
     * @see #addr
     */
    public EndpointVirtualIP withAddr(String addr) {
        this.addr = addr;
        return this;
    }

    @Override
    public String toString() {
        return "EndpointVirtualIP{" +
                "networkID='" + networkID + '\'' +
                ", addr='" + addr + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EndpointVirtualIP that = (EndpointVirtualIP) o;
        return Objects.equals(networkID, that.networkID) &&
                Objects.equals(addr, that.addr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(networkID, addr);
    }
}
