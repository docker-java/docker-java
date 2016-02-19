package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;

import java.util.Map;

/**
 * @see Container
 * @since {@link RemoteApiVersion#VERSION_1_22}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContainerNetworkSettings {
    @JsonProperty("Networks")
    private Map<String, ContainerNetwork> networks;

    public Map<String, ContainerNetwork> getNetworks() {
        return networks;
    }
}
