package com.github.dockerjava.api.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A node as returned by the /events API, for instance, when Swarm is used.
 */
@JsonInclude(Include.NON_NULL)
public class Node {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("ID")
    private String id;

    @JsonProperty("Addr")
    private String addr;

    @JsonProperty("IP")
    private String ip;

    @JsonProperty("Labels")
    private Map<String, String> labels;

    @JsonProperty("Cpus")
    private int cpus;

    @JsonProperty("Memory")
    private int memory;

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

    public Map<String, String> getLabels() {
        return labels;
    }

    public int getCpus() {
        return cpus;
    }

    public int getMemory() {
        return memory;
    }

}
