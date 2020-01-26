package com.github.dockerjava.api.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;

public class NetworkingConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("EndpointsConfig")
    private Map<String, ContainerNetwork> endpointsConfig;

    public Map<String, ContainerNetwork> getEndpointsConfig() {
        return endpointsConfig;
    }

    public NetworkingConfig withEndpointsConfig(Map<String, ContainerNetwork> endpointsConfig) {
        this.endpointsConfig = endpointsConfig;
        return this;
    }
}
