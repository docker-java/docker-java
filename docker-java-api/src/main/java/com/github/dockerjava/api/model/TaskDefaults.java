package com.github.dockerjava.api.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode
@ToString
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
}
