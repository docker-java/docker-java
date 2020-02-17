package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * Used in {@link Statistics}
 *
 * @author Yuting Liu
 */
public class MemoryStatsConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("stats")
    private StatsConfig stats;

    @JsonProperty("usage")
    private Long usage;

    @JsonProperty("max_usage")
    private Long maxUsage;

    @JsonProperty("failcnt")
    private Long failcnt;

    @JsonProperty("limit")
    private Long limit;

    /**
     * @see #stats
     */
    @CheckForNull
    public StatsConfig getStats() {
        return stats;
    }

    /**
     * @see #usage
     */
    @CheckForNull
    public Long getUsage() {
        return usage;
    }

    /**
     * @see #maxUsage
     */
    @CheckForNull
    public Long getMaxUsage() {
        return maxUsage;
    }

    /**
     * @see #failcnt
     */
    public Long getFailcnt() {
        return failcnt;
    }

    /**
     * @see #limit
     */
    @CheckForNull
    public Long getLimit() {
        return limit;
    }
}
