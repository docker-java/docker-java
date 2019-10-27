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
public class TaskDefaults implements Serializable {

    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("LogDriver")
    private Driver logDriver;

    /**
     * @see #logDriver
     */
    @CheckForNull
    public Driver getLogDriver() {
        return logDriver;
    }

    /**
     * @see #logDriver
     */
    public TaskDefaults withLogDriver(Driver logDriver) {
        this.logDriver = logDriver;
        return this;
    }

    @Override
    public String toString() {
        return "TaskDefaults{" +
                "logDriver=" + logDriver +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskDefaults that = (TaskDefaults) o;
        return Objects.equals(logDriver, that.logDriver);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logDriver);
    }
}
