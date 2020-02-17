package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * Used in {@link Statistics}
 *
 * @author Yuting Liu
 */
public class CpuStatsConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("cpu_usage")
    private CpuUsageConfig cpuUsage;

    @JsonProperty("system_cpu_usage")
    private Long systemCpuUsage;

    @JsonProperty("online_cpus")
    private Long onlineCpus;

    @JsonProperty("throttling_data")
    private ThrottlingDataConfig throttlingData;

    /**
     * @see #cpuUsage
     */
    @CheckForNull
    public CpuUsageConfig getCpuUsage() {
        return cpuUsage;
    }

    /**
     * @see #systemCpuUsage
     */
    @CheckForNull
    public Long getSystemCpuUsage() {
        return systemCpuUsage;
    }

    /**
     * @see #onlineCpus
     */
    @CheckForNull
    public Long getOnlineCpus() {
        return onlineCpus;
    }

    /**
     * @see #throttlingData
     */
    @CheckForNull
    public ThrottlingDataConfig getThrottlingData() {
        return throttlingData;
    }
}
