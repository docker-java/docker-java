package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Used for Listing services.
 *
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
public class Service implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("ID")
    private String id;

    /**
     * @since 1.24
     */
    @JsonProperty("CreatedAt")
    private Date createdAt;

    /**
     * @since 1.24
     */
    @JsonProperty("UpdatedAt")
    private Date updatedAt;

    /**
     * @since 1.24
     */
    @JsonProperty("Spec")
    private ServiceSpec spec;

    /**
     * @since 1.24
     */
    @JsonProperty("Endpoint")
    private Endpoint endpoint;

    /**
     * @since 1.24
     */
    @JsonProperty("UpdateStatus")
    private ServiceUpdateStatus updateStatus;

    /**
     * @since 1.24
     */
    @JsonProperty("Version")
    private ResourceVersion version;

    /**
     * @see #id
     */
    @CheckForNull
    public String getId() {
        return id;
    }

    /**
     * @see #id
     */
    public Service withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * @see #createdAt
     */
    @CheckForNull
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @see #createdAt
     */
    public Service withCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * @see #updatedAt
     */
    @CheckForNull
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @see #updatedAt
     */
    public Service withUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * @see #spec
     */
    @CheckForNull
    public ServiceSpec getSpec() {
        return spec;
    }

    /**
     * @see #spec
     */
    public Service withSpec(ServiceSpec spec) {
        this.spec = spec;
        return this;
    }

    /**
     * @see #endpoint
     */
    @CheckForNull
    public Endpoint getEndpoint() {
        return endpoint;
    }

    /**
     * @see #endpoint
     */
    public Service withEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    /**
     * @see #updateStatus
     */
    @CheckForNull
    public ServiceUpdateStatus getUpdateStatus() {
        return updateStatus;
    }

    /**
     * @see #updateStatus
     */
    public Service withUpdateStatus(ServiceUpdateStatus updateStatus) {
        this.updateStatus = updateStatus;
        return this;
    }

    /**
     * @see #version
     */
    @CheckForNull
    public ResourceVersion getVersion() {
        return version;
    }

    /**
     * @see #version
     */
    public Service withVersion(ResourceVersion version) {
        this.version = version;
        return this;
    }
}
