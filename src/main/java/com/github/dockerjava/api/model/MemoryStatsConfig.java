package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * Used in {@link Statistics}
 *
 * @author Yuting Liu
 */

@JsonIgnoreProperties(ignoreUnknown = true)
class MemoryStatsConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("usage")
    private Long usage;

    @JsonProperty("max_usage")
    private Long maxUsage;

    @JsonProperty("stats")
    private StatsConfig stats;

    @JsonProperty("limit")
    private Long limit;

    /**
     * @see #usage
     */
    @CheckForNull
    public Long getUsage() {
        return usage;
    }

    /**
     * @see #usage
     */
    public MemoryStatsConfig withUsage(Long usage) {
        this.usage = usage;
        return this;
    }

    /**
     * @see #maxUsage
     */
    @CheckForNull
    public Long getMaxUsage() {
        return maxUsage;
    }

    /**
     * @see #maxUsage
     */
    public MemoryStatsConfig withMaxUsage(Long maxUsage) {
        this.maxUsage = maxUsage;
        return this;
    }

    /**
     * @see #stats
     */
    @CheckForNull
    public StatsConfig getStats() {
        return stats;
    }

    /**
     * @see #stats
     */
    public MemoryStatsConfig withStats(StatsConfig stats) {
        this.stats = stats;
        return this;
    }

    /**
     * @see #limit
     */
    @CheckForNull
    public Long getLimit() {
        return limit;
    }

    /**
     * @see #limit
     */
    public MemoryStatsConfig withLimit(Long limit) {
        this.limit = limit;
        return this;
    }


}
