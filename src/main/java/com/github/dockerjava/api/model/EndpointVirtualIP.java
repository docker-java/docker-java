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
public class EndpointVirtualIP implements Serializable {
    public static final Long serialVersionUID = 1L;

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
