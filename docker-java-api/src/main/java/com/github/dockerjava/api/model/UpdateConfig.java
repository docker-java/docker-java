package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public class UpdateConfig implements Serializable {
    public static final long serialVersionUID = 1L;

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
        return "UpdateConfig{" +
                "parallelism=" + parallelism +
                ", delay=" + delay +
                ", failureAction=" + failureAction +
                ", maxFailureRatio=" + maxFailureRatio +
                ", monitor=" + monitor +
                ", order=" + order +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateConfig that = (UpdateConfig) o;
        return Objects.equals(parallelism, that.parallelism) &&
                Objects.equals(delay, that.delay) &&
                failureAction == that.failureAction &&
                Objects.equals(maxFailureRatio, that.maxFailureRatio) &&
                Objects.equals(monitor, that.monitor) &&
                order == that.order;
    }

    @Override
    public int hashCode() {
        return Objects.hash(parallelism, delay, failureAction, maxFailureRatio, monitor, order);
    }
}
