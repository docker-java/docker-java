package com.github.dockerjava.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Configuration object for copy command.
 * @author Victor Lyuboslavsky
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CopyConfig {

    @JsonProperty("HostPath")
    private String hostPath;

    @JsonProperty("Resource")
    private String resource;

    /**
     * Constructor.
     */
    public CopyConfig() {
        hostPath = ".";
    }

    /**
     * Retrieves the 'resource' variable.
     * @return the 'resource' variable value
     */
    public String getResource() {
        return resource;
    }

    /**
     * Sets the 'resource' variable.
     * @param resource the new 'resource' variable value to set
     */
    public void setResource(String resource) {
        this.resource = resource;
    }

    /**
     * Retrieves the 'hostPath' variable.
     * @return the 'hostPath' variable value
     */
    public String getHostPath() {
        return hostPath;
    }

    /**
     * Sets the 'hostPath' variable.
     * @param hostPath the new 'hostPath' variable value to set
     */
    public void setHostPath(String hostPath) {
        this.hostPath = hostPath;
    }

    @Override
    public String toString() {
        return "{\"HostPath\":\"" + hostPath + "\", \"Resource\":\"" + resource + "\"}";
    }

}
