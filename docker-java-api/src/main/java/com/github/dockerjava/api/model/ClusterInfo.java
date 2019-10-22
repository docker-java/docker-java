package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


/**
 * Used for creating swarms.
 *
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClusterInfo implements Serializable {

    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("CreatedAt")
    private Date createdAt;

    /**
     * @since 1.24
     */
    @JsonProperty("Spec")
    private SwarmSpec spec;

    /**
     * @since 1.24
     */
    @JsonProperty("ID")
    private String id;

    /**
     * @since 1.24
     */
    @JsonProperty("UpdatedAt")
    private Date updatedAt;

    /**
     * @since 1.24
     */
    @JsonProperty("Version")
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

    @Override
    public String toString() {
        return "ClusterInfo{" +
                "createdAt=" + createdAt +
                ", spec=" + spec +
                ", id='" + id + '\'' +
                ", updatedAt=" + updatedAt +
                ", version=" + version +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClusterInfo that = (ClusterInfo) o;
        return Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(spec, that.spec) &&
                Objects.equals(id, that.id) &&
                Objects.equals(updatedAt, that.updatedAt) &&
                Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, spec, id, updatedAt, version);
    }
}
