package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SwarmNodeDescription implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Hostname")
    private String hostname;

    /**
     * @since 1.24
     */
    @JsonProperty("Platform")
    private SwarmNodePlatform platform;

    /**
     * @since 1.24
     */
    @JsonProperty("Resources")
    private SwarmNodeResources resources;

    /**
     * @since 1.24
     */
    @JsonProperty("Engine")
    private SwarmNodeEngineDescription engine;

    /**
     * @see #hostname
     */
    @CheckForNull
    public String getHostname() {
        return hostname;
    }

    /**
     * @see #hostname
     */
    public SwarmNodeDescription withHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    /**
     * @see #platform
     */
    @CheckForNull
    public SwarmNodePlatform getPlatform() {
        return platform;
    }

    /**
     * @see #platform
     */
    public SwarmNodeDescription withPlatform(SwarmNodePlatform platform) {
        this.platform = platform;
        return this;
    }

    /**
     * @see #resources
     */
    @CheckForNull
    public SwarmNodeResources getResources() {
        return resources;
    }

    /**
     * @see #resources
     */
    public SwarmNodeDescription withResources(SwarmNodeResources resources) {
        this.resources = resources;
        return this;
    }

    /**
     * @see #engine
     */
    @CheckForNull
    public SwarmNodeEngineDescription getEngine() {
        return engine;
    }

    /**
     * @see #engine
     */
    public SwarmNodeDescription withEngine(SwarmNodeEngineDescription engine) {
        this.engine = engine;
        return this;
    }

    @Override
    public String toString() {
        return "SwarmNodeDescription{" +
                "hostname='" + hostname + '\'' +
                ", platform=" + platform +
                ", resources=" + resources +
                ", engine=" + engine +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SwarmNodeDescription that = (SwarmNodeDescription) o;
        return Objects.equals(hostname, that.hostname) &&
                Objects.equals(platform, that.platform) &&
                Objects.equals(resources, that.resources) &&
                Objects.equals(engine, that.engine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hostname, platform, resources, engine);
    }
}
