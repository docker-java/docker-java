package com.github.dockerjava.api.command;

import com.github.dockerjava.api.annotation.FieldName;
import java.util.List;

public class HealthState {

    @FieldName("Status")
    private String status;

    @FieldName("FailingStreak")
    private Integer failingStreak;

    @FieldName("Log")
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
