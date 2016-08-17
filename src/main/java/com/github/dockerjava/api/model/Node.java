package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Used for Listing containers.
 *
 * @author Martin Root
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Node {
    @JsonProperty("Id")
    private String id;

    @JsonProperty("Version")
    private String[] version;

    @JsonProperty("CreatedAt")
    private String createdAt;

    @JsonProperty("UpdatedAt")
    private String updatedAt;

    @JsonProperty("Spec")
    private NodeSpec spec;

    @JsonProperty("Description")
    private NodeDescription description;

    @JsonProperty("Status")
    private NodeStatus status;

    @JsonProperty("ManagerStatus")
    private NodeManagerStatus managerStatus;

    public String getId() {
        return id;
    }

    public String[] getVersion() {
        return version;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public NodeSpec getSpec() {
        return spec;
    }

    public NodeDescription getDescription() {
        return description;
    }

    public NodeStatus getStatus() {
        return status;
    }

    public NodeManagerStatus getManagerStatus() {
        return managerStatus;
    }
}
