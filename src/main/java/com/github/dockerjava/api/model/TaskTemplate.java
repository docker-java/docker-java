package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * For ServiceSpec
 */
public class TaskTemplate {

    @JsonProperty("ContainerSpec")
    private ContainerSpec containerSpec;

    @JsonProperty("Resources")
    private TaskResourcesSpec resources;

    @JsonProperty("RestartPolicy")
    private ServiceRestartPolicy restartPolicy;

    @JsonProperty("Placement")
    private ServicePlacement placement;

    public ContainerSpec getContainerSpec() {
        return containerSpec;
    }

    public TaskResourcesSpec getResources() {
        return resources;
    }

    public ServiceRestartPolicy getRestartPolicy() {
        return restartPolicy;
    }

    public ServicePlacement getPlacement() {
        return placement;
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
