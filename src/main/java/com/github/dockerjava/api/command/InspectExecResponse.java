package com.github.dockerjava.api.command;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InspectExecResponse {
    @JsonProperty("ID")
    private String id;

    @JsonProperty("OpenStdin")
    private Boolean openStdin;

    @JsonProperty("OpenStderr")
    private Boolean openStderr;

    @JsonProperty("OpenStdout")
    private Boolean openStdout;

    @JsonProperty("Running")
    private Boolean running;

    @JsonProperty("ExitCode")
    private Integer exitCode;

    public String getId() {
        return id;
    }

    public Boolean isOpenStdin() {
        return openStdin;
    }

    public Boolean isOpenStderr() {
        return openStderr;
    }

    public Boolean isOpenStdout() {
        return openStdout;
    }

    public Boolean isRunning() {
        return running;
    }

    public Integer getExitCode() {
        return exitCode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class ProcessConfig {
        @JsonProperty("arguments")
        private List<String> arguments;

        @JsonProperty("entrypoint")
        private String entryPoint;

        @JsonProperty("privileged")
        private Boolean privileged;

        @JsonProperty("tty")
        private Boolean tty;

        @JsonProperty("user")
        private String user;

        public List<String> getArguments() {
            return arguments;
        }

        public String getEntryPoint() {
            return entryPoint;
        }

        public Boolean isPrivileged() {
            return privileged;
        }

        public Boolean isTty() {
            return tty;
        }

        public String getUser() {
            return user;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }
}
