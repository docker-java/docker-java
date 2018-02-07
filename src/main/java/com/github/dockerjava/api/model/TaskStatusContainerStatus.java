package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskStatusContainerStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("ContainerID")
    private String containerID = null;

    @JsonProperty("PID")
    private Integer pid = null;

    @JsonProperty("ExitCode")
    private Integer exitCode = null;

    public String getContainerID() {
        return containerID;
    }

    public Integer getPid() {
        return pid;
    }

    public Integer getExitCode() {
        return exitCode;
    }

    public TaskStatusContainerStatus withContainerID(String containerID) {
        this.containerID = containerID;
        return this;
    }

    public TaskStatusContainerStatus withPid(Integer pid) {
        this.pid = pid;
        return this;
    }

    public TaskStatusContainerStatus withExitCode(Integer exitCode) {
        this.exitCode = exitCode;
        return this;
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
}
