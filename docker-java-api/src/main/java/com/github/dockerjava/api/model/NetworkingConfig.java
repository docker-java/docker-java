package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.util.Map;

/**
 * Networking configuration for a container.
 */
public class NetworkingConfig {

    public NetworkingConfig() {
        this.endpointsConfig = new HashMap<>();
    }
    public NetworkingConfig(Map<String, ContainerNetwork> endpointsConfig) {
        this.endpointsConfig = endpointsConfig;
    }

    @JsonProperty("EndpointsConfig")
    private Map<String, ContainerNetwork> endpointsConfig;

    @CheckForNull
    public Map<String, ContainerNetwork> getEndpointsConfig() {
        return endpointsConfig;
    }

    public NetworkingConfig withEndpointsConfig(Map<String, ContainerNetwork> endpointsConfig) {
        this.endpointsConfig = endpointsConfig;
        return this;
    }
}
