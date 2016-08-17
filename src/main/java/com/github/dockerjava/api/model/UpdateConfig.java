package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class UpdateConfig {

    @JsonProperty("Parallelism")
    private long parallelism;

    @JsonProperty("Delay")
    private long delay;

    @JsonProperty("FailureAction")
    private UpdateFailureAction failureAction;

    public long getParallelism() {
        return parallelism;
    }

    public UpdateConfig withParallelism(long parallelism) {
        this.parallelism = parallelism;
        return this;
    }

    public long getDelay() {
        return delay;
    }

    public UpdateConfig setDelay(long delay) {
        this.delay = delay;
        return this;
    }

    public UpdateFailureAction getFailureAction() {
        return failureAction;
    }

    public UpdateConfig withFailureAction(UpdateFailureAction failureAction) {
        this.failureAction = failureAction;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
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
