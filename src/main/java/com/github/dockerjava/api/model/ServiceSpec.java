package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.List;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public class ServiceSpec implements Serializable {
    public static final Long serialVersionUID = 1L;

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
    @JsonProperty("Configs")
    private List<NetworkAttachmentConfig> networks;

    /**
     * @since 1.24
     */
    @JsonProperty("EndpointSpec")
    private EndpointSpec endpointSpec;

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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
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
