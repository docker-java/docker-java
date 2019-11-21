package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Date;


/**
 * Used for creating swarms.
 *
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
public class ClusterInfo implements Serializable {

    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @FieldName("CreatedAt")
    private Date createdAt;

    /**
     * @since 1.24
     */
    @FieldName("Spec")
    private SwarmSpec spec;

    /**
     * @since 1.24
     */
    @FieldName("ID")
    private String id;

    /**
     * @since 1.24
     */
    @FieldName("UpdatedAt")
    private Date updatedAt;

    /**
     * @since 1.24
     */
    @FieldName("Version")
    private ResourceVersion version;

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
    public ClusterInfo withCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * @see #spec
     */
    @CheckForNull
    public SwarmSpec getSpec() {
        return spec;
    }

    /**
     * @see #spec
     */
    public ClusterInfo withSpec(SwarmSpec spec) {
        this.spec = spec;
        return this;
    }
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
    public ClusterInfo withId(String id) {
        this.id = id;
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
    public ClusterInfo withUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
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
    public ClusterInfo withVersion(ResourceVersion version) {
        this.version = version;
        return this;
    }
}
