package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public class ServiceRestartPolicy implements Serializable {
    public static final Long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Condition")
    private ServiceRestartCondition condition;

    /**
     * @since 1.24
     */
    @JsonProperty("Delay")
    private Long delay;

    /**
     * @since 1.24
     */
    @JsonProperty("MaxAttempts")
    private Long maxAttempts;

    /**
     * @since 1.24
     */
    @JsonProperty("Window")
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
