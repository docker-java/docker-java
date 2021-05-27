package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.model.DockerObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString
public class HealthStateLog extends DockerObject {

    @JsonProperty("Start")
    private String start;

    @JsonProperty("End")
    private String end;

    @JsonProperty("ExitCode")
    private Long exitCode;

    @JsonProperty("Output")
    private String output;

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    /**
     *
     * @deprecated use {@link #getExitCodeLong()}
     */
    @Deprecated
    public Integer getExitCode() {
        return exitCode != null ? exitCode.intValue() : null;
    }

    public Long getExitCodeLong() {
        return exitCode;
    }

    public String getOutput() {
        return output;
    }
}
