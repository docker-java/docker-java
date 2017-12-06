package com.github.dockerjava.api.model;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.CheckForNull;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of a Docker statistics.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class Statistics implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("read")
    private String read;

    /**
     * @since Docker Remote API 1.21
     */
    @CheckForNull
    @JsonProperty("networks")
    private Map<String, StatisticNetWorksConfig> networks;

    /**
     * @deprecated as of Docker Remote API 1.21, replaced by {@link #networks}
     */
    @Deprecated
    @JsonProperty("network")
    private Map<String, StatisticNetWorksConfig> network;

    @JsonProperty("memory_stats")
    private MemoryStatsConfig memoryStats;

    @JsonProperty("blkio_stats")
    private Map<String, Object> blkioStats;

    @JsonProperty("cpu_stats")
    private Map<String, Object> cpuStats;

    /**
     * @since Docker Remote API 1.19
     */
    @JsonProperty("precpu_stats")
    private Map<String, Object> preCpuStats;

    public String getRead() {
        return read;
    }

    /**
     * @since Docker Remote API 1.21
     */
    @CheckForNull
    public Map<String, StatisticNetWorksConfig> getNetworks() {
        return networks;
    }

    /**
     * @deprecated as of Docker Remote API 1.21, replaced by {@link #getNetworks()}
     */
    @Deprecated
    public Map<String, StatisticNetWorksConfig> getNetwork() {
        return network;
    }

    public Map<String, Object> getCpuStats() {
        return cpuStats;
    }

    /**
     * The cpu statistic of last read, which is used for calculating the cpu usage percent.
     * It is not the exact copy of the {@link #getCpuStats()}.
     */
    public Map<String, Object> getPreCpuStats() {
        return preCpuStats;
    }

    public MemoryStatsConfig getMemoryStats() {
        return memoryStats;
    }

    public Map<String, Object> getBlkioStats() {
        return blkioStats;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
