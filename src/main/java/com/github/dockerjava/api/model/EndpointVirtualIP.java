package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class EndpointVirtualIP {

    @JsonProperty("NetworkID")
    private String networkID;

    @JsonProperty("Addr")
    private String addr;

    public String getNetworkID() {
        return networkID;
    }

    public EndpointVirtualIP withNetworkID(String networkID) {
        this.networkID = networkID;
        return this;
    }

    public String getAddr() {
        return addr;
    }

    public EndpointVirtualIP withAddr(String addr) {
        this.addr = addr;
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
