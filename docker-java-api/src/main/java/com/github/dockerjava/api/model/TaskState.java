package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FromPrimitive;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public enum TaskState {

    NEW("new"),

    ALLOCATED("allocated"),

    PENDING("pending"),

    ASSIGNED("assigned"),

    ACCEPTED("accepted"),

    PREPARING("preparing"),

    READY("ready"),

    STARTING("starting"),

    RUNNING("running"),

    COMPLETE("complete"),

    SHUTDOWN("shutdown"),

    FAILED("failed"),

    REJECTED("rejected"),

    REMOVE("remove"),

    ORPHANED("orphaned");

    private String value;

    TaskState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @FromPrimitive
    public static TaskState forValue(String s) {
        return TaskState.valueOf(s.toUpperCase());
    }

    @Override
    public String toString() {
        return value;
    }
}
