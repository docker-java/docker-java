package com.github.dockerjava.api.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NodeEngine {

    @JsonProperty("EngineVersion")
    private String engineVersion;

    @JsonProperty("Labels")
    private Map<String,String> labels;

    @JsonProperty("Plugins")
    private NodePlugin[] plugins;

    public String getEngineVersion() {
        return engineVersion;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public NodePlugin[] getPlugins() {
        return plugins;
    }

    public void setEngineVersion(String engineVersion) {
        this.engineVersion = engineVersion;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public void setPlugins(NodePlugin[] plugins) {
        this.plugins = plugins;
    }
}
