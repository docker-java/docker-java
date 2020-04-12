package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@EqualsAndHashCode
@ToString
public class Plugin implements Serializable {
    private static final long serialVersionUID = 1L;
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

    public String getName() {
        return name;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public PluginSetting getSettings() {
        return settings;
    }

    public String getPluginReference() {
        return pluginReference;
    }

    public PluginConfig getConfig() {
        return config;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setSettings(PluginSetting settings) {
        this.settings = settings;
    }

    public void setPluginReference(String pluginReference) {
        this.pluginReference = pluginReference;
    }

    public void setConfig(PluginConfig config) {
        this.config = config;
    }
}
