package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * A node as returned by the /events API, for instance, when Swarm is used.
 */
@JsonInclude(Include.NON_NULL)
public class Node implements Serializable {
    private static final long serialVersionUID = 1L;

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
