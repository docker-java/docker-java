package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InspectExecResponse {
    @JsonProperty("ID")
    private String id;

    @JsonProperty("OpenStdin")
    private boolean openStdin;

    @JsonProperty("OpenStderr")
    private boolean openStderr;

    @JsonProperty("OpenStdout")
    private boolean openStdout;

    @JsonProperty("Running")
    private boolean running;

    public String getId() {
        return id;
    }

    public boolean isOpenStdin() {
        return openStdin;
    }

    public boolean isOpenStderr() {
        return openStderr;
    }

    public boolean isOpenStdout() {
        return openStdout;
    }

    public boolean isRunning() {
        return running;
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
        private boolean privileged;

        @JsonProperty("tty")
        private boolean tty;

        @JsonProperty("user")
        private String user;

        public List<String> getArguments() {
            return arguments;
        }

        public String getEntryPoint() {
            return entryPoint;
        }

        public boolean isPrivileged() {
            return privileged;
        }

        public boolean isTty() {
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
