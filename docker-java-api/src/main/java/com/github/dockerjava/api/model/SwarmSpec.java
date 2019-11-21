package com.github.dockerjava.api.model;


import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
public class SwarmSpec implements Serializable {

    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @FieldName("Dispatcher")
    private SwarmDispatcherConfig dispatcher;

    /**
     * @since 1.24
     */
    @FieldName("Orchestration")
    private SwarmOrchestration orchestration;


    /**
     * @since 1.24
     */
    @FieldName("CAConfig")
    private SwarmCAConfig caConfig;

    /**
     * @since 1.24
     */
    @FieldName("Raft")
    private SwarmRaftConfig raft;

    /**
     * @since 1.24
     */
    @FieldName("TaskDefaults")
    private TaskDefaults taskDefaults;

    /**
     * @since 1.24
     */
    @FieldName("Name")
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
}
