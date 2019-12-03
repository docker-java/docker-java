package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
public class ServiceUpdateStatus implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("State")
    private ServiceUpdateState state;

    /**
     * @since 1.24
     */
    @JsonProperty("StartedAt")
    private Date startedAt;

    /**
     * @since 1.24
     */
    @JsonProperty("CompletedAt")
    private Date completedAt;

    /**
     * @since 1.24
     */
    @JsonProperty("Message")
    private String message;

    /**
     * @see #state
     */
    @CheckForNull
    public ServiceUpdateState getState() {
        return state;
    }

    /**
     * @see #state
     */
    public ServiceUpdateStatus withState(ServiceUpdateState state) {
        this.state = state;
        return this;
    }

    /**
     * @see #startedAt
     */
    @CheckForNull
    public Date getStartedAt() {
        return startedAt;
    }

    /**
     * @see #startedAt
     */
    public ServiceUpdateStatus withStartedAt(Date startedAt) {
        this.startedAt = startedAt;
        return this;
    }

    /**
     * @see #completedAt
     */
    @CheckForNull
    public Date getCompletedAt() {
        return completedAt;
    }

    /**
     * @see #completedAt
     */
    public ServiceUpdateStatus withCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
        return this;
    }

    /**
     * @see #message
     */
    @CheckForNull
    public String getMessage() {
        return message;
    }

    /**
     * @see #message
     */
    public ServiceUpdateStatus withMessage(String message) {
        this.message = message;
        return this;
    }
}
