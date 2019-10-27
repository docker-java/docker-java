package com.github.dockerjava.api.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    @Override
    public String toString() {
        return "SwarmNodeEngineDescription{" +
                "engineVersion='" + engineVersion + '\'' +
                ", labels=" + labels +
                ", plugins=" + Arrays.toString(plugins) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SwarmNodeEngineDescription that = (SwarmNodeEngineDescription) o;
        return Objects.equals(engineVersion, that.engineVersion) &&
                Objects.equals(labels, that.labels) &&
                Arrays.equals(plugins, that.plugins);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(engineVersion, labels);
        result = 31 * result + Arrays.hashCode(plugins);
        return result;
    }
}
