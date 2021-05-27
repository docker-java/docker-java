package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.model.DockerObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString
public class HealthState extends DockerObject {

    @JsonProperty("Status")
    private String status;

    @JsonProperty("FailingStreak")
    private Integer failingStreak;

    @JsonProperty("Log")
    private List<HealthStateLog> log;

    public String getStatus() {
        return status;
    }

    public Integer getFailingStreak() {
        return failingStreak;
    }

    public List<HealthStateLog> getLog() {
        return log;
    }
}
