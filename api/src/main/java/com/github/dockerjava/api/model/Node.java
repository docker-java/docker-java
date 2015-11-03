package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A node as returned by the /events API, for instance, when Swarm is used.
 */
public class Node {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Addr")
    private String addr;

    @JsonProperty("Ip")
    private String ip;

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
}
