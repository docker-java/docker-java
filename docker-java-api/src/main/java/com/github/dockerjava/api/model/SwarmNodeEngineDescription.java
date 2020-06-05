package com.github.dockerjava.api.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Map;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
public class SwarmNodeEngineDescription implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("EngineVersion")
    private String engineVersion;

    /**
     * @since 1.24
     */
    @JsonProperty("Labels")
    private Map<String, String> labels;

    /**
     * @since 1.24
     */
    @JsonProperty("Plugins")
    private SwarmNodePluginDescription[] plugins;

    /**
     * @see #engineVersion
     */
    @CheckForNull
    public String getEngineVersion() {
        return engineVersion;
    }

    /**
     * @see #engineVersion
     */
    public SwarmNodeEngineDescription withEngineVersion(String engineVersion) {
        this.engineVersion = engineVersion;
        return this;
    }

    /**
     * @see #labels
     */
    @CheckForNull
    public Map<String, String> getLabels() {
        return labels;
    }

    /**
     * @see #labels
     */
    public SwarmNodeEngineDescription withLabels(Map<String, String> labels) {
        this.labels = labels;
        return this;
    }

    /**
     * @see #plugins
     */
    @CheckForNull
    public SwarmNodePluginDescription[] getPlugins() {
        return plugins;
    }

    /**
     * @see #plugins
     */
    public SwarmNodeEngineDescription withPlugins(SwarmNodePluginDescription[] plugins) {
        this.plugins = plugins;
        return this;
    }
}
