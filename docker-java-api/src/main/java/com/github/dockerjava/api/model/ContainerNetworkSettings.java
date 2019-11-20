package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * Sub-object in {@link Container}
 *
 * @see Container
 * @since {@link RemoteApiVersion#VERSION_1_22}
 */
@EqualsAndHashCode
@ToString
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
}
