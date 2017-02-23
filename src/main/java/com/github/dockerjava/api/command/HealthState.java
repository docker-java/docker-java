package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HealthState {

    @JsonProperty("Status")
    private String status;

    @JsonProperty("FailingStreak")
    private Integer failingStreak;

    @JsonProperty("Log")
    private List<Log> log;

    public String getStatus() {
        return status;
    }

    public Integer getFailingStreak() {
        return failingStreak;
    }

    public List<Log> getLog() {
        return log;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Log {

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
}
