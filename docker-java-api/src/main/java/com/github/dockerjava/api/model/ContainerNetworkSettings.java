package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

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
        return "ContainerNetworkSettings{" +
                "networks=" + networks +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContainerNetworkSettings that = (ContainerNetworkSettings) o;
        return Objects.equals(networks, that.networks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(networks);
    }
}
