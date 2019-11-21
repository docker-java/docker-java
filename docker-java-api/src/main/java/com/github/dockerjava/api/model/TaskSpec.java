package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.List;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
public class TaskSpec implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @FieldName("ContainerSpec")
    private ContainerSpec containerSpec;

    /**
     * @since 1.24
     */
    @FieldName("Resources")
    private ResourceRequirements resources;

    /**
     * @since 1.24
     */
    @FieldName("RestartPolicy")
    private ServiceRestartPolicy restartPolicy;

    /**
     * @since 1.24
     */
    @FieldName("Placement")
    private ServicePlacement placement;

    /**
     * @since 1.24
     */
    @FieldName("LogDriver")
    private Driver logDriver;

    /**
     * @since 1.24
     * a counter that triggers an update even if no relevant parameters have been changed
     * a random value will work if it is incrementing.
     */
    @FieldName("ForceUpdate")
    private Integer forceUpdate;

    /**
     * @since 1.25
     */
    @FieldName("Networks")
    private List<NetworkAttachmentConfig> networks;

    /**
     * @since 1.30
     */
    @FieldName("Runtime")
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
}
