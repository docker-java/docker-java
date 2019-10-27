package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceSpec implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Name")
    private String name;

    /**
     * @since 1.24
     */
    @JsonProperty("TaskTemplate")
    private TaskSpec taskTemplate;

    /**
     * @since 1.24
     */
    @JsonProperty("Mode")
    private ServiceModeConfig mode;

    /**
     * @since 1.24
     */
    @JsonProperty("UpdateConfig")
    private UpdateConfig updateConfig;

    /**
     * @since 1.24
     */
    @JsonProperty("Networks")
    private List<NetworkAttachmentConfig> networks;

    /**
     * @since 1.24
     */
    @JsonProperty("EndpointSpec")
    private EndpointSpec endpointSpec;

    /**
     * @since 1.24
     */
    @JsonProperty("Labels")
    private Map<String, String> labels;

    /**
     * @since 1.28
     */
    @JsonProperty("RollbackConfig")
    private UpdateConfig rollbackConfig;

    /**
     * @see #name
     */
    public String getName() {
        return name;
    }

    /**
     * @see #name
     */
    public ServiceSpec withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @see #taskTemplate
     */
    @CheckForNull
    public TaskSpec getTaskTemplate() {
        return taskTemplate;
    }

    /**
     * @see #taskTemplate
     */
    public ServiceSpec withTaskTemplate(TaskSpec taskTemplate) {
        this.taskTemplate = taskTemplate;
        return this;
    }

    /**
     * @see #mode
     */
    @CheckForNull
    public ServiceModeConfig getMode() {
        return mode;
    }

    /**
     * @see #mode
     */
    public ServiceSpec withMode(ServiceModeConfig mode) {
        this.mode = mode;
        return this;
    }

    /**
     * @see #updateConfig
     */
    @CheckForNull
    public UpdateConfig getUpdateConfig() {
        return updateConfig;
    }

    /**
     * @see #updateConfig
     */
    public ServiceSpec withUpdateConfig(UpdateConfig updateConfig) {
        this.updateConfig = updateConfig;
        return this;
    }

    /**
     * @see #networks
     */
    @CheckForNull
    public List<NetworkAttachmentConfig> getNetworks() {
        return networks;
    }

    /**
     * @see #networks
     */
    public ServiceSpec withNetworks(List<NetworkAttachmentConfig> networks) {
        this.networks = networks;
        return this;
    }

    /**
     * @see #endpointSpec
     */
    @CheckForNull
    public EndpointSpec getEndpointSpec() {
        return endpointSpec;
    }

    /**
     * @see #endpointSpec
     */
    public ServiceSpec withEndpointSpec(EndpointSpec endpointSpec) {
        this.endpointSpec = endpointSpec;
        return this;
    }

    /**
     * @see #labels
     */
    public ServiceSpec withLabels(Map<String, String> labels) {
        this.labels = labels;
        return this;
    }

    public UpdateConfig getRollbackConfig() {
        return rollbackConfig;
    }

    public ServiceSpec withRollbackConfig(UpdateConfig rollbackConfig) {
        this.rollbackConfig = rollbackConfig;
        return this;
    }

    /**
     * @see #labels
     */
    @CheckForNull
    public Map<String, String> getLabels() {
        return labels;
    }

    @Override
    public String toString() {
        return "ServiceSpec{" +
                "name='" + name + '\'' +
                ", taskTemplate=" + taskTemplate +
                ", mode=" + mode +
                ", updateConfig=" + updateConfig +
                ", networks=" + networks +
                ", endpointSpec=" + endpointSpec +
                ", labels=" + labels +
                ", rollbackConfig=" + rollbackConfig +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceSpec that = (ServiceSpec) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(taskTemplate, that.taskTemplate) &&
                Objects.equals(mode, that.mode) &&
                Objects.equals(updateConfig, that.updateConfig) &&
                Objects.equals(networks, that.networks) &&
                Objects.equals(endpointSpec, that.endpointSpec) &&
                Objects.equals(labels, that.labels) &&
                Objects.equals(rollbackConfig, that.rollbackConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, taskTemplate, mode, updateConfig, networks, endpointSpec, labels, rollbackConfig);
    }
}
