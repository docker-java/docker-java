package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskSpec implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("ContainerSpec")
    private ContainerSpec containerSpec;

    /**
     * @since 1.24
     */
    @JsonProperty("Resources")
    private ResourceRequirements resources;

    /**
     * @since 1.24
     */
    @JsonProperty("RestartPolicy")
    private ServiceRestartPolicy restartPolicy;

    /**
     * @since 1.24
     */
    @JsonProperty("Placement")
    private ServicePlacement placement;

    /**
     * @since 1.24
     */
    @JsonProperty("LogDriver")
    private Driver logDriver;

    /**
     * @since 1.24
     * a counter that triggers an update even if no relevant parameters have been changed
     * a random value will work if it is incrementing.
     */
    @JsonProperty("ForceUpdate")
    private Integer forceUpdate;

    /**
     * @since 1.25
     */
    @JsonProperty("Networks")
    private List<NetworkAttachmentConfig> networks;

    /**
     * @since 1.30
     */
    @JsonProperty("Runtime")
    private String runtime;

    /**
     * @see #containerSpec
     */
    @CheckForNull
    public ContainerSpec getContainerSpec() {
        return containerSpec;
    }

    /**
     * @see #containerSpec
     */
    public TaskSpec withContainerSpec(ContainerSpec containerSpec) {
        this.containerSpec = containerSpec;
        return this;
    }

    /**
     * @see #resources
     */
    @CheckForNull
    public ResourceRequirements getResources() {
        return resources;
    }

    /**
     * @see #resources
     */
    public TaskSpec withResources(ResourceRequirements resources) {
        this.resources = resources;
        return this;
    }

    /**
     * @see #restartPolicy
     */
    @CheckForNull
    public ServiceRestartPolicy getRestartPolicy() {
        return restartPolicy;
    }

    /**
     * @see #restartPolicy
     */
    public TaskSpec withRestartPolicy(ServiceRestartPolicy restartPolicy) {
        this.restartPolicy = restartPolicy;
        return this;
    }

    /**
     * @see #placement
     */
    @CheckForNull
    public ServicePlacement getPlacement() {
        return placement;
    }

    /**
     * @see #placement
     */
    public TaskSpec withPlacement(ServicePlacement placement) {
        this.placement = placement;
        return this;
    }

    public String getRuntime() {
        return runtime;
    }

    public TaskSpec withRuntime(String runtime) {
        this.runtime = runtime;
        return this;
    }

    /**
     * @see #logDriver
     */
    @CheckForNull
    public Driver getLogDriver() {
        return logDriver;
    }

    /**
     * @see #logDriver
     */
    public TaskSpec withLogDriver(Driver logDriver) {
        this.logDriver = logDriver;
        return this;
    }

    /**
     * @see #forceUpdate
     */
    @CheckForNull
    public Integer getForceUpdate() {
        return forceUpdate;
    }

    /**
     * @see #forceUpdate
     */
    public TaskSpec withForceUpdate(Integer forceUpdate) {
        this.forceUpdate = forceUpdate;
        return this;
    }

    public List<NetworkAttachmentConfig> getNetworks() {
        return networks;
    }

    public TaskSpec withNetworks(List<NetworkAttachmentConfig> networks) {
        this.networks = networks;
        return this;
    }

    @Override
    public String toString() {
        return "TaskSpec{" +
                "containerSpec=" + containerSpec +
                ", resources=" + resources +
                ", restartPolicy=" + restartPolicy +
                ", placement=" + placement +
                ", logDriver=" + logDriver +
                ", forceUpdate=" + forceUpdate +
                ", networks=" + networks +
                ", runtime='" + runtime + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskSpec taskSpec = (TaskSpec) o;
        return Objects.equals(containerSpec, taskSpec.containerSpec) &&
                Objects.equals(resources, taskSpec.resources) &&
                Objects.equals(restartPolicy, taskSpec.restartPolicy) &&
                Objects.equals(placement, taskSpec.placement) &&
                Objects.equals(logDriver, taskSpec.logDriver) &&
                Objects.equals(forceUpdate, taskSpec.forceUpdate) &&
                Objects.equals(networks, taskSpec.networks) &&
                Objects.equals(runtime, taskSpec.runtime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(containerSpec, resources, restartPolicy, placement, logDriver, forceUpdate, networks, runtime);
    }
}
