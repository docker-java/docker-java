package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * A node as returned by the /events API, for instance, when Swarm is used.
 */
@EqualsAndHashCode
@ToString
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

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
