package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class ExecCreateCmdResponse {

    @JsonProperty("Id")
    private String id;

    public String getId() {
        return id;
    }
}
