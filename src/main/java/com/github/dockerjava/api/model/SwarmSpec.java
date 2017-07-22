package com.github.dockerjava.api.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SwarmSpec implements Serializable {

    public static final Long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Dispatcher")
    private SwarmDispatcherConfig dispatcher;

    /**
     * @since 1.24
     */
    @JsonProperty("Orchestration")
    private SwarmOrchestration orchestration;


    /**
     * @since 1.24
     */
    @JsonProperty("CAConfig")
    private SwarmCAConfig caConfig;

    /**
     * @since 1.24
     */
    @JsonProperty("Raft")
    private SwarmRaftConfig raft;

    /**
     * @since 1.24
     */
    @JsonProperty("TaskDefaults")
    private TaskDefaults taskDefaults;

    /**
     * @since 1.24
     */
    @JsonProperty("Name")
    private String name;

    /**
     * @see #dispatcher
     */
    @CheckForNull
    public SwarmDispatcherConfig getDispatcher() {
        return dispatcher;
    }

    /**
     * @see #dispatcher
     */
    public SwarmSpec withDispatcher(SwarmDispatcherConfig dispatcher) {
        this.dispatcher = dispatcher;
        return this;
    }

    /**
     * @see #orchestration
     */
    @CheckForNull
    public SwarmOrchestration getOrchestration() {
        return orchestration;
    }

    /**
     * @see #orchestration
     */
    public SwarmSpec withOrchestration(SwarmOrchestration orchestration) {
        this.orchestration = orchestration;
        return this;
    }

    /**
     * @see #caConfig
     */
    @CheckForNull
    public SwarmCAConfig getCaConfig() {
        return caConfig;
    }

    /**
     * @see #caConfig
     */
    public SwarmSpec withCaConfig(SwarmCAConfig caConfig) {
        this.caConfig = caConfig;
        return this;
    }

    /**
     * @see #raft
     */
    @CheckForNull
    public SwarmRaftConfig getRaft() {
        return raft;
    }

    /**
     * @see #raft
     */
    public SwarmSpec withRaft(SwarmRaftConfig raft) {
        this.raft = raft;
        return this;
    }

    /**
     * @see #taskDefaults
     */
    @CheckForNull
    public TaskDefaults getTaskDefaults() {
        return taskDefaults;
    }

    /**
     * @see #taskDefaults
     */
    public SwarmSpec withTaskDefaults(TaskDefaults taskDefaults) {
        this.taskDefaults = taskDefaults;
        return this;
    }

    /**
     * @see #name
     */
    @CheckForNull
    public String getName() {
        return name;
    }

    /**
     * @see #name
     */
    public SwarmSpec withName(String name) {
        this.name = name;
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
