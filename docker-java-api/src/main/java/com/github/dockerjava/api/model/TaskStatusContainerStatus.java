package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
public class TaskStatusContainerStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("ContainerID")
    private String containerID = null;

    @JsonProperty("PID")
    private Long pid = null;

    @JsonProperty("ExitCode")
    private Long exitCode = null;

    public String getContainerID() {
        return containerID;
    }

    /**
     *
     * @deprecated use {@link #getPidLong()}
     */
    @Deprecated
    public Integer getPid() {
        return pid != null ? pid.intValue() : null;
    }

    public Long getPidLong() {
        return pid;
    }

    /**
     * @deprecated use {@link #getExitCodeLong()}
     */
    @Deprecated
    public Integer getExitCode() {
        return exitCode != null ? exitCode.intValue() : null;
    }

    public Long getExitCodeLong() {
        return exitCode;
    }

    public TaskStatusContainerStatus withContainerID(String containerID) {
        this.containerID = containerID;
        return this;
    }

    public TaskStatusContainerStatus withPid(Long pid) {
        this.pid = pid;
        return this;
    }

    /**
     *
     * @deprecated use {@link #withPid(Long)}
     */
    @Deprecated
    public TaskStatusContainerStatus withPid(Integer pid) {
        this.pid = pid != null ? pid.longValue() : null;
        return this;
    }

    public TaskStatusContainerStatus withExitCode(Long exitCode) {
        this.exitCode = exitCode;
        return this;
    }

    /**
     *
     * @deprecated use {@link #withExitCode(Long)}
     */
    @Deprecated
    public TaskStatusContainerStatus withExitCode(Integer exitCode) {
        this.exitCode = exitCode != null ? exitCode.longValue() : null;
        return this;
    }
}
