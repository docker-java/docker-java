package com.github.dockerjava.api.command;

import com.github.dockerjava.api.annotation.FieldName;

public class HealthStateLog {

    @FieldName("Start")
    private String start;

    @FieldName("End")
    private String end;

    @FieldName("ExitCode")
    private Long exitCode;

    @FieldName("Output")
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
