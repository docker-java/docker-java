package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Map;

/**
 * Sub-object in {@link Container}
 *
 * @see Container
 * @since {@link RemoteApiVersion#VERSION_1_22}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContainerNetworkSettings implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("Networks")
    private Map<String, ContainerNetwork> networks;

    /**
     * @see #networks
     */
    public Map<String, ContainerNetwork> getNetworks() {
        return networks;
    }

    /**
     * @see #networks
     */
    public ContainerNetworkSettings withNetworks(Map<String, ContainerNetwork> networks) {
        this.networks = networks;
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
