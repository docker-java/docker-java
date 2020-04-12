package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.model.PluginConfig;
import com.github.dockerjava.api.model.PluginSetting;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class InspectPluginResponse {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Enabled")
    private Boolean enabled;

    @JsonProperty("Settings")
    private PluginSetting settings;

    @JsonProperty("PluginReference")
    private String pluginReference;

    @JsonProperty("Config")
    private PluginConfig config;

    public String getId() {
        return id;
    }

    public InspectPluginResponse withId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public InspectPluginResponse withName(String name) {
        this.name = name;
        return this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public InspectPluginResponse withEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public PluginSetting getSettings() {
        return settings;
    }

    public InspectPluginResponse withSettings(PluginSetting settings) {
        this.settings = settings;
        return this;
    }

    public String getPluginReference() {
        return pluginReference;
    }

    public InspectPluginResponse withPluginReference(String pluginReference) {
        this.pluginReference = pluginReference;
        return this;
    }

    public PluginConfig getConfig() {
        return config;
    }

    public InspectPluginResponse withConfig(PluginConfig config) {
        this.config = config;
        return this;
    }

}
