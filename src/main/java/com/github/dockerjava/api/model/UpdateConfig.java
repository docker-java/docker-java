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
public class UpdateConfig implements Serializable {
    public static final Long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Parallelism")
    private Long parallelism;

    /**
     * @since 1.24
     */
    @JsonProperty("Delay")
    private Long delay;

    /**
     * @since 1.24
     */
    @JsonProperty("FailureAction")
    private UpdateFailureAction failureAction;

    /**
     * @since 1.25
     */
    @JsonProperty("MaxFailureRatio")
    private Float maxFailureRatio;

    /**
     * @since 1.25
     */
    @JsonProperty("Monitor")
    private Long monitor;

    /**
     * @since 1.36
     */
    @JsonProperty("Order")
    private UpdateOrder order;


    /**
     * @see #parallelism
     */
    @CheckForNull
    public Long getParallelism() {
        return parallelism;
    }

    /**
     * @see #parallelism
     */
    public UpdateConfig withParallelism(long parallelism) {
        this.parallelism = parallelism;
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
    public UpdateConfig setDelay(Long delay) {
        this.delay = delay;
        return this;
    }

    /**
     * @see #failureAction
     */
    @CheckForNull
    public UpdateFailureAction getFailureAction() {
        return failureAction;
    }

    /**
     * @see #failureAction
     */
    public UpdateConfig withFailureAction(UpdateFailureAction failureAction) {
        this.failureAction = failureAction;
        return this;
    }

    public UpdateConfig withParallelism(Long parallelism) {
        this.parallelism = parallelism;
        return this;
    }

    public UpdateConfig withDelay(Long delay) {
        this.delay = delay;
        return this;
    }

    public Float getMaxFailureRatio() {
        return maxFailureRatio;
    }

    public UpdateConfig withMaxFailureRatio(Float maxFailureRatio) {
        this.maxFailureRatio = maxFailureRatio;
        return this;
    }

    public Long getMonitor() {
        return monitor;
    }

    public UpdateConfig withMonitor(Long monitor) {
        this.monitor = monitor;
        return this;
    }

    public UpdateOrder getOrder() {
        return order;
    }

    public UpdateConfig withOrder(UpdateOrder order) {
        this.order = order;
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
