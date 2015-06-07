package com.github.dockerjava.api.model;

import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of a Docker statistics.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Statistics {
    
    @JsonProperty("read")
    private String read;
    
    @JsonProperty("network")
    private Map<String,Object> networkStats;

    @JsonProperty("memory_stats")
    private Map<String,Object> memoryStats;
    
    @JsonProperty("blkio_stats")
    private Map<String,Object> blkioStats;
    
    @JsonProperty("cpu_stats")
    private Map<String,Object> cpuStats;

    public Map<String,Object> getNetworkStats() {
        return networkStats;
    }
    
    public Map<String,Object> getCpuStats() {
        return cpuStats;
    }
    
    public Map<String,Object> getMemoryStats() {
        return memoryStats;
    }
    
    public Map<String,Object> getBlkioStats() {
        return blkioStats;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
