package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * Used in {@link Statistics}
 *
 * @author Yuting Liu
 */
public class CpuStatsConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldName("cpu_usage")
    private CpuUsageConfig cpuUsage;

    @FieldName("system_cpu_usage")
    private Long systemCpuUsage;

    @FieldName("online_cpus")
    private Long onlineCpus;

    @FieldName("throttling_data")
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
