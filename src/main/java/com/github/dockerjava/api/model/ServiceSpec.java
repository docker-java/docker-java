package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * for Service
 */
public class ServiceSpec {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("TaskTemplate")
    private TaskTemplate taskTemplate;

    @JsonProperty("Mode")
    private ServiceModeConfig mode;

    @JsonProperty("UpdateConfig")
    private UpdateConfig updateConfig;

    @JsonProperty("EndpointSpec")
    private EndpointSpec endpointSpec;

    public String getName() {
        return name;
    }

    public ServiceSpec withName(String name) {
        this.name = name;
        return this;
    }

    public TaskTemplate getTaskTemplate() {
        return taskTemplate;
    }

    public ServiceSpec withTaskTemplate(TaskTemplate taskTemplate) {
        this.taskTemplate = taskTemplate;
        return this;
    }

    public ServiceModeConfig getMode() {
        return mode;
    }

    public ServiceSpec withMode(ServiceModeConfig mode) {
        this.mode = mode;
        return this;
    }

    public UpdateConfig getUpdateConfig() {
        return updateConfig;
    }

    public ServiceSpec withUpdateConfig(UpdateConfig updateConfig) {
        this.updateConfig = updateConfig;
        return this;
    }

    public EndpointSpec getEndpointSpec() {
        return endpointSpec;
    }

    public ServiceSpec withEndpointSpec(EndpointSpec endpointSpec) {
        this.endpointSpec = endpointSpec;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
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
