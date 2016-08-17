package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * For TaskTemplate
 */
public class ServiceRestartPolicy {

    @JsonProperty("Condition")
    private ServiceRestartCondition condition;

    @JsonProperty("Delay")
    private Long delay;

    @JsonProperty("MaxAttempts")
    private Long maxAttempts;

    @JsonProperty("Window")
    private Long window;

    public ServiceRestartCondition getCondition() {
        return condition;
    }

    public Long getDelay() {
        return delay;
    }

    public Long getMaxAttempts() {
        return maxAttempts;
    }

    public Long getWindow() {
        return window;
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
