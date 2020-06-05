package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class HealthState {

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
