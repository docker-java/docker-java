package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
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
}
