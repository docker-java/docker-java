package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import static org.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE;

/**
 * Used for Listing SwarmNodes.
 * @since 1.24
 * @author Martin Root
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SwarmNode {
    @JsonProperty("Id")
    private String id;

    @JsonProperty("Version")
    private String[] version;

    @JsonProperty("CreatedAt")
    private String createdAt;

    @JsonProperty("UpdatedAt")
    private String updatedAt;

    @JsonProperty("Spec")
    private SwarmNodeSpec spec;

    @JsonProperty("Description")
    private SwarmNodeDescription description;

    @JsonProperty("Status")
    private SwarmNodeStatus status;

    @JsonProperty("ManagerStatus")
    private SwarmNodeManagerStatus managerStatus;

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

    public SwarmNodeSpec getSpec() {
        return spec;
    }

    public SwarmNodeDescription getDescription() {
        return description;
    }

    public SwarmNodeStatus getStatus() {
        return status;
    }

    public SwarmNodeManagerStatus getManagerStatus() {
        return managerStatus;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVersion(String[] version) {
        this.version = version;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setSpec(SwarmNodeSpec spec) {
        this.spec = spec;
    }

    public void setDescription(SwarmNodeDescription description) {
        this.description = description;
    }

    public void setStatus(SwarmNodeStatus status) {
        this.status = status;
    }

    public void setManagerStatus(SwarmNodeManagerStatus managerStatus) {
        this.managerStatus = managerStatus;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, SHORT_PREFIX_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
