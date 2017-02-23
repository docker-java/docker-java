package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HealthStateLog {

    @JsonProperty("Start")
    private String start;

    @JsonProperty("End")
    private String end;

    @JsonProperty("ExitCode")
    private Integer exitCode;

    @JsonProperty("Output")
    private String output;

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public Integer getExitCode() {
        return exitCode;
    }

    public String getOutput() {
        return output;
    }
}
