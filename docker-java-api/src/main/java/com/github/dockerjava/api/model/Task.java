package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
public class Task extends DockerObject implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("ID")
    private String id = null;

    @JsonProperty("Version")
    private ObjectVersion version = null;

    @JsonProperty("CreatedAt")
    private Date createdAt = null;

    @JsonProperty("UpdatedAt")
    private Date updatedAt = null;

    @JsonProperty("Spec")
    private TaskSpec spec = null;

    @JsonProperty("ServiceID")
    private String serviceId = null;

    @JsonProperty("Slot")
    private Integer slot = null;

    @JsonProperty("NodeID")
    private String nodeId = null;

    @JsonProperty("AssignedGenericResources")
    private List<GenericResource> assignedGenericResources = null;

    @JsonProperty("Status")
    private TaskStatus status = null;

    @JsonProperty("DesiredState")
    private TaskState desiredState = null;

    @JsonProperty("NetworksAttachments")
    private List<NetworkAttachment> networkAttachments;


    /**
     * The ID of the task.
     *
     * @return ID
     **/
    public String getId() {
        return id;
    }

    /**
     * Get version
     *
     * @return version
     **/
    public ObjectVersion getVersion() {
        return version;
    }

    /**
     * Get createdAt
     *
     * @return createdAt
     **/
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Get updatedAt
     *
     * @return updatedAt
     **/
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Get spec
     *
     * @return spec
     **/
    public TaskSpec getSpec() {
        return spec;
    }

    /**
     * Get status
     *
     * @return status
     **/
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * Get desiredState
     *
     * @return desiredState
     **/
    public TaskState getDesiredState() {
        return desiredState;
    }

    /**
     * The ID of the service this task is part of.
     *
     * @return serviceId
     **/
    public String getServiceId() {
        return serviceId;
    }

    /**
     * Get slot
     *
     * @return slot
     **/
    public Integer getSlot() {
        return slot;
    }

    /**
     * The ID of the node that this task is on.
     *
     * @return nodeId
     **/
    public String getNodeId() {
        return nodeId;
    }

    /**
     * Getter for the network attachments
     *
     * @return networkAttachments
     */
    public List<NetworkAttachment> getNetworkAttachments() {
        return networkAttachments;
    }


    public Task withId(String id) {
        this.id = id;
        return this;
    }

    public Task withVersion(ObjectVersion version) {
        this.version = version;
        return this;
    }

    public Task withCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Task withUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Task withSpec(TaskSpec spec) {
        this.spec = spec;
        return this;
    }

    public Task withServiceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public Task withSlot(Integer slot) {
        this.slot = slot;
        return this;
    }

    public Task withNodeId(String nodeId) {
        this.nodeId = nodeId;
        return this;
    }

    public Task withAssignedGenericResources(List<GenericResource> assignedGenericResources) {
        this.assignedGenericResources = assignedGenericResources;
        return this;
    }

    public Task withStatus(TaskStatus status) {
        this.status = status;
        return this;
    }

    public Task withDesiredState(TaskState desiredState) {
        this.desiredState = desiredState;
        return this;
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}
