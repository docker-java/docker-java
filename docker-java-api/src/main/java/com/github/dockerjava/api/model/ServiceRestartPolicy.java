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
public class ServiceRestartPolicy implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @FieldName("Condition")
    private ServiceRestartCondition condition;

    /**
     * @since 1.24
     */
    @FieldName("Delay")
    private Long delay;

    /**
     * @since 1.24
     */
    @FieldName("MaxAttempts")
    private Long maxAttempts;

    /**
     * @since 1.24
     */
    @FieldName("Window")
    private Long window;

    /**
     * @see #condition
     */
    @CheckForNull
    public ServiceRestartCondition getCondition() {
        return condition;
    }

    /**
     * @see #condition
     */
    public ServiceRestartPolicy withCondition(ServiceRestartCondition condition) {
        this.condition = condition;
        return this;
    }

    /**
     * @see #delay
     */
    @CheckForNull
    public Long getDelay() {
        return delay;
    }

    /**
     * @see #delay
     */
    public ServiceRestartPolicy withDelay(Long delay) {
        this.delay = delay;
        return this;
    }

    /**
     * @see #maxAttempts
     */
    @CheckForNull
    public Long getMaxAttempts() {
        return maxAttempts;
    }

    /**
     * @see #maxAttempts
     */
    public ServiceRestartPolicy withMaxAttempts(Long maxAttempts) {
        this.maxAttempts = maxAttempts;
        return this;
    }

    /**
     * @see #window
     */
    @CheckForNull
    public Long getWindow() {
        return window;
    }

    /**
     * @see #window
     */
    public ServiceRestartPolicy withWindow(Long window) {
        this.window = window;
        return this;
    }
}
