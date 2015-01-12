package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExecCreateCmdResponse {

    @JsonProperty("Id")
    private String id;

    public String getId() {
        return id;
    }
}
