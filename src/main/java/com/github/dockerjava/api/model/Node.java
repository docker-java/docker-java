package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A node as returned by the /events API, for instance, when Swarm is used.
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Node {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("ID")
    private String id;

    @JsonProperty("Addr")
    private String addr;

    @JsonProperty("IP")
    private String ip;

    @JsonProperty("Cpus")
    private String cpus;

    @JsonProperty("Memory")
    private String memory;

    @JsonProperty("Labels")
    private Labels labels;

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

    public Labels getLabels() {
        return labels;
    }

}
