package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Used for Listing config.
 *
 * @since {@link RemoteApiVersion#VERSION_1_30}
 */
@ToString
@EqualsAndHashCode(callSuper = true)
public class Config extends DockerObject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * @since 1.30
     */
    @JsonProperty("ID")
    private String id;

    /**
     * @since 1.30
     */
    @JsonProperty("CreatedAt")
    private Date createdAt;

    /**
     * @since 1.30
     */
    @JsonProperty("UpdatedAt")
    private Date updatedAt;

    /**
     * @since 1.30
     */
    @JsonProperty("Spec")
    private ConfigSpec spec;

    /**
     * @since 1.30
     */
    @JsonProperty("Version")
    private ResourceVersion version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ConfigSpec getSpec() {
        return spec;
    }

    public void setSpec(ConfigSpec spec) {
        this.spec = spec;
    }

    public ResourceVersion getVersion() {
        return version;
    }

    public void setVersion(ResourceVersion version) {
        this.version = version;
    }
}
