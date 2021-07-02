package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.model.DockerObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class ExecCreateCmdResponse extends DockerObject {

    @JsonProperty("Id")
    private String id;

    public String getId() {
        return id;
    }
}
