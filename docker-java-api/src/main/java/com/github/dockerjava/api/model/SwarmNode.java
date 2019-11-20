package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Date;


/**
 * Used for Listing SwarmNodes.
 *
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
public class SwarmNode implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("ID")
    private String id;

    /**
     * @since 1.24
     */
    @JsonProperty("Version")
    private ObjectVersion version;

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
    private SwarmNodeSpec spec;

    /**
     * @since 1.24
     */
    @JsonProperty("Description")
    private SwarmNodeDescription description;

    /**
     * @since 1.24
     */
    @JsonProperty("Status")
    private SwarmNodeStatus status;

    /**
     * @since 1.24
     */
    @JsonProperty("ManagerStatus")
    private SwarmNodeManagerStatus managerStatus;

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
    public SwarmNode withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * @see #version
     */
    @CheckForNull
    public ObjectVersion getVersion() {
        return version;
    }

    /**
     * @see #version
     */
    public SwarmNode withVersion(ObjectVersion version) {
        this.version = version;
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
    public SwarmNode withCreatedAt(Date createdAt) {
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
    public SwarmNode withUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * @see #spec
     */
    @CheckForNull
    public SwarmNodeSpec getSpec() {
        return spec;
    }

    /**
     * @see #spec
     */
    public SwarmNode withSpec(SwarmNodeSpec spec) {
        this.spec = spec;
        return this;
    }

    /**
     * @see #description
     */
    @CheckForNull
    public SwarmNodeDescription getDescription() {
        return description;
    }

    /**
     * @see #description
     */
    public SwarmNode withDescription(SwarmNodeDescription description) {
        this.description = description;
        return this;
    }

    /**
     * @see #status
     */
    @CheckForNull
    public SwarmNodeStatus getStatus() {
        return status;
    }

    /**
     * @see #status
     */
    public SwarmNode withStatus(SwarmNodeStatus status) {
        this.status = status;
        return this;
    }

    /**
     * @see #managerStatus
     */
    @CheckForNull
    public SwarmNodeManagerStatus getManagerStatus() {
        return managerStatus;
    }

    /**
     * @see #managerStatus
     */
    public SwarmNode withManagerStatus(SwarmNodeManagerStatus managerStatus) {
        this.managerStatus = managerStatus;
        return this;
    }
}
