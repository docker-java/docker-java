package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
public class TaskStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldName("Timestamp")
    private String timestamp = null;

    @FieldName("State")
    private TaskState state = null;

    @FieldName("Message")
    private String message = null;

    @FieldName("Err")
    private String err = null;

    @FieldName("ContainerStatus")
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
}
