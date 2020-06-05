package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * Used in {@link Statistics}
 *
 * @author Yuting Liu
 */
public class ThrottlingDataConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("periods")
    private Long periods;

    @JsonProperty("throttled_periods")
    private Long throttledPeriods;

    @JsonProperty("throttled_time")
    private Long throttledTime;

    /**
     * @see #periods
     */
    @CheckForNull
    public Long getPeriods() {
        return periods;
    }

    /**
     * @see #throttledPeriods
     */
    @CheckForNull
    public Long getThrottledPeriods() {
        return throttledPeriods;
    }

    /**
     * @see #throttledTime
     */
    @CheckForNull
    public Long getThrottledTime() {
        return throttledTime;
    }
}
