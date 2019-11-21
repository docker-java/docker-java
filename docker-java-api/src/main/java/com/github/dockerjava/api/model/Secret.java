package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;

import java.io.Serializable;
import java.util.Date;

/**
 * Used for Listing secret.
 *
 * @since {@link RemoteApiVersion#VERSION_1_25}
 */
public class Secret implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @FieldName("ID")
    private String id;

    /**
     * @since 1.24
     */
    @FieldName("CreatedAt")
    private Date createdAt;

    /**
     * @since 1.24
     */
    @FieldName("UpdatedAt")
    private Date updatedAt;

    /**
     * @since 1.24
     */
    @FieldName("Spec")
    private ServiceSpec spec;

    /**
     * @since 1.24
     */
    @FieldName("Version")
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

    public ServiceSpec getSpec() {
        return spec;
    }

    public void setSpec(ServiceSpec spec) {
        this.spec = spec;
    }

    public ResourceVersion getVersion() {
        return version;
    }

    public void setVersion(ResourceVersion version) {
        this.version = version;
    }
}
