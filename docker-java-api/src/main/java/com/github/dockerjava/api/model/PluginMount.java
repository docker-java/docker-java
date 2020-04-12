package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@EqualsAndHashCode
@ToString
public class PluginMount implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Description")
    private String id;

    @JsonProperty("Settable")
    private String[] settable;

    @JsonProperty("Source")
    private String source;

    @JsonProperty("Destination")
    private String destination;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Options")
    private String[] options;

    public String getName() {
        return name;
    }

    public PluginMount withName(String name) {
        this.name = name;
        return this;
    }

    public String getId() {
        return id;
    }

    public PluginMount withId(String id) {
        this.id = id;
        return this;
    }

    public String[] getSettable() {
        return settable;
    }

    public PluginMount withSettable(String[] settable) {
        this.settable = settable;
        return this;
    }

    public String getSource() {
        return source;
    }

    public PluginMount withSource(String source) {
        this.source = source;
        return this;
    }

    public String getDestination() {
        return destination;
    }

    public PluginMount withDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public String getType() {
        return type;
    }

    public PluginMount withType(String type) {
        this.type = type;
        return this;
    }

    public String[] getOptions() {
        return options;
    }

    public PluginMount withOptions(String[] options) {
        this.options = options;
        return this;
    }
}
