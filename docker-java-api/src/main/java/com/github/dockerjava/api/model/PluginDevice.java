package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@EqualsAndHashCode
@ToString
public class PluginDevice implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Settable")
    private String[] settable;

    @JsonProperty("Path")
    private String path;

    public String getName() {
        return name;
    }

    public PluginDevice withName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PluginDevice withDescription(String description) {
        this.description = description;
        return this;
    }

    public String[] getSettable() {
        return settable;
    }

    public PluginDevice withSettable(String[] settable) {
        this.settable = settable;
        return this;
    }

    public String getPath() {
        return path;
    }

    public PluginDevice withPath(String path) {
        this.path = path;
        return this;
    }
}
