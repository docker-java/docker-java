package com.github.dockerjava.api.model;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class AuthConfigurations extends DockerObject implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("configs")
    private Map<String, AuthConfig> configs = new TreeMap<>();

    public void addConfig(AuthConfig authConfig) {
        configs.put(authConfig.getRegistryAddress(), authConfig);
    }

    public Map<String, AuthConfig> getConfigs() {
        return this.configs;
    }

}
