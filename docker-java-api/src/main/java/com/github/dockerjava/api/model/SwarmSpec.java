package com.github.dockerjava.api.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SwarmSpec implements Serializable {

    public static final long serialVersionUID = 1L;

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
        return "SwarmSpec{" +
                "dispatcher=" + dispatcher +
                ", orchestration=" + orchestration +
                ", caConfig=" + caConfig +
                ", raft=" + raft +
                ", taskDefaults=" + taskDefaults +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SwarmSpec swarmSpec = (SwarmSpec) o;
        return Objects.equals(dispatcher, swarmSpec.dispatcher) &&
                Objects.equals(orchestration, swarmSpec.orchestration) &&
                Objects.equals(caConfig, swarmSpec.caConfig) &&
                Objects.equals(raft, swarmSpec.raft) &&
                Objects.equals(taskDefaults, swarmSpec.taskDefaults) &&
                Objects.equals(name, swarmSpec.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dispatcher, orchestration, caConfig, raft, taskDefaults, name);
    }
}
