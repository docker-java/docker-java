package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("ID")
    private String id = null;

    @JsonProperty("Version")
    private ObjectVersion version = null;

    @JsonProperty("CreatedAt")
    private String createdAt = null;

    @JsonProperty("UpdatedAt")
    private String updatedAt = null;

    @JsonProperty("Name")
    private String name = null;

    @JsonProperty("Labels")
    private Map<String, String> labels = null;

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
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Get updatedAt
     *
     * @return updatedAt
     **/
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Name of the task.
     *
     * @return name
     **/

    public String getName() {
        return name;
    }

    /**
     * User-defined key/value metadata.
     *
     * @return labels
     **/
    public Map<String, String> getLabels() {
        return labels;
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

    public Task withId(String id) {
        this.id = id;
        return this;
    }

    public Task withVersion(ObjectVersion version) {
        this.version = version;
        return this;
    }

    public Task withCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Task withUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Task withName(String name) {
        this.name = name;
        return this;
    }

    public Task withLabels(Map<String, String> labels) {
        this.labels = labels;
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

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task task = (Task) o;
        return Objects.equals(this.id, task.id) &&
                Objects.equals(this.version, task.version) &&
                Objects.equals(this.createdAt, task.createdAt) &&
                Objects.equals(this.updatedAt, task.updatedAt) &&
                Objects.equals(this.name, task.name) &&
                Objects.equals(this.labels, task.labels) &&
                Objects.equals(this.spec, task.spec) &&
                Objects.equals(this.serviceId, task.serviceId) &&
                Objects.equals(this.slot, task.slot) &&
                Objects.equals(this.nodeId, task.nodeId) &&
                Objects.equals(this.assignedGenericResources, task.assignedGenericResources) &&
                Objects.equals(this.status, task.status) &&
                Objects.equals(this.desiredState, task.desiredState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, createdAt, updatedAt, name, labels, spec, serviceId, slot,
                nodeId, assignedGenericResources, status, desiredState);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Task {\n");
        sb.append("    ID: ").append(toIndentedString(id)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
        sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
        sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    labels: ").append(toIndentedString(labels)).append("\n");
        sb.append("    spec: ").append(toIndentedString(spec)).append("\n");
        sb.append("    serviceId: ").append(toIndentedString(serviceId)).append("\n");
        sb.append("    slot: ").append(toIndentedString(slot)).append("\n");
        sb.append("    nodeId: ").append(toIndentedString(nodeId)).append("\n");
        sb.append("    assignedGenericResources: ").append(toIndentedString(assignedGenericResources)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    desiredState: ").append(toIndentedString(desiredState)).append("\n");
        sb.append("}");
        return sb.toString();
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
