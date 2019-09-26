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
public class TaskStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("Timestamp")
    private String timestamp = null;

    @JsonProperty("State")
    private TaskState state = null;

    @JsonProperty("Message")
    private String message = null;

    @JsonProperty("Err")
    private String err = null;

    @JsonProperty("ContainerStatus")
    private TaskStatusContainerStatus containerStatus = null;

    public String getTimestamp() {
        return timestamp;
    }

    public TaskStatus withTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public TaskState getState() {
        return state;
    }

    public TaskStatus withState(TaskState state) {
        this.state = state;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public TaskStatus withMessage(String message) {
        this.message = message;
        return this;
    }

    public String getErr() {
        return err;
    }

    public TaskStatus withErr(String err) {
        this.err = err;
        return this;
    }

    public TaskStatusContainerStatus getContainerStatus() {
        return containerStatus;
    }

    public TaskStatus withContainerStatus(TaskStatusContainerStatus containerStatus) {
        this.containerStatus = containerStatus;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
}
