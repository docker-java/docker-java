package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * Used in {@link Statistics}
 *
 * @author Yuting Liu
 */
@EqualsAndHashCode
@ToString
public class MemoryStatsConfig extends DockerObject implements Serializable {
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

    @JsonProperty("commitbytes")
    private Long commitbytes;

    @JsonProperty("commitpeakbytes")
    private Long commitpeakbytes;

    @JsonProperty("privateworkingset")
    private Long privateworkingset;

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

    /**
     * @see #commitbytes
     */
    public Long getCommitbytes() {
        return commitbytes;
    }

    /**
     * @see #commitpeakbytes
     */
    public Long getCommitpeakbytes() {
        return commitpeakbytes;
    }

    /**
     * @see #privateworkingset
     */
    public Long getPrivateworkingset() {
        return privateworkingset;
    }
}
