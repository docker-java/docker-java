package com.github.dockerjava.api.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NodeManagerStatus {

    @JsonProperty("Leader")
    private boolean leader;

    @JsonProperty("Reachability")
    private String reachability;

    @JsonProperty("Addr")
    private String addr;

    public boolean isLeader() {
        return leader;
    }

    public String getReachability() {
        return reachability;
    }

    public String getAddr() {
        return addr;
    }
}
