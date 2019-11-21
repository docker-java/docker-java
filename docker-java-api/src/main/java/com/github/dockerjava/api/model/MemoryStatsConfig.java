package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * Used in {@link Statistics}
 *
 * @author Yuting Liu
 */
public class MemoryStatsConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldName("stats")
    private StatsConfig stats;

    @FieldName("usage")
    private Long usage;

    @FieldName("max_usage")
    private Long maxUsage;

    @FieldName("failcnt")
    private Long failcnt;

    @FieldName("limit")
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
