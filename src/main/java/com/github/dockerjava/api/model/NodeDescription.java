package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NodeDescription {
    @JsonProperty("Hostname")
    private String hostname;

    @JsonProperty("Platform")
    private NodePlatform platform;

    @JsonProperty("Resources")
    private NodeResources resources;

    @JsonProperty("Engine")
    private NodeEngine engine;

    public String getHostname() {
        return hostname;
    }

    public NodePlatform getPlatform() {
        return platform;
    }

    public NodeResources getResources() {
        return resources;
    }

    public NodeEngine getEngine() {
        return engine;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setPlatform(NodePlatform platform) {
        this.platform = platform;
    }

    public void setResources(NodeResources resources) {
        this.resources = resources;
    }

    public void setEngine(NodeEngine engine) {
        this.engine = engine;
    }
}
