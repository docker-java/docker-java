package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Map;

import java.io.Serializable;

/**
 * A node as returned by the /events API, for instance, when Swarm is used.
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Node implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("ID")
    private String id;

    @JsonProperty("Addr")
    private String addr;

    @JsonProperty("IP")
    private String ip;

    @JsonProperty("Cpus")
    private Integer cpus;

    @JsonProperty("Memory")
    private Long memory;

    @JsonProperty("Labels")
    private Map<String, String> labels;

    @JsonProperty("Version")
    private String version;

    @JsonProperty("DeltaDuration")
    private Long deltaDuration;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getAddr() {
        return addr;
    }

    public String getIp() {
        return ip;
    }

    public Integer getCpus() {
        return cpus;
    }

    public Long getMemory() {
        return memory;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public String getVersion() {
        return version;
    }

    public Long getDeltaDuration() {
        return deltaDuration;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
