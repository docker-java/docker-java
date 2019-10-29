package com.github.dockerjava.api.command;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.model.NetworkSettings;

import javax.annotation.CheckForNull;

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

    /**
     * @since {@link RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("CanRemove")
    private Boolean canRemove;

    @JsonProperty("ExitCode")
    private Long exitCode;

    @JsonProperty("ProcessConfig")
    private ProcessConfig processConfig;

    /**
     * @deprecated @since {@link RemoteApiVersion#VERSION_1_22}
     */
    @Deprecated
    @JsonProperty("Container")
    private Container container;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("ContainerID")
    private String containerID;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("DetachKeys")
    private String detachKeys;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_25}
     */
    @JsonProperty("Pid")
    private Long pid;

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

    /**
     * @deprecated use {@link #getExitCodeLong()}
     */
    @Deprecated
    public Integer getExitCode() {
        return exitCode != null ? exitCode.intValue() : null;
    }

    public Long getExitCodeLong() {
        return exitCode;
    }

    public ProcessConfig getProcessConfig() {
        return processConfig;
    }

    /**
     * @see #container
     */
    @Deprecated
    public Container getContainer() {
        return container;
    }

    /**
     * @see #canRemove
     */
    @CheckForNull
    public Boolean getCanRemove() {
        return canRemove;
    }

    /**
     * @see #containerID
     */
    @CheckForNull
    public String getContainerID() {
        return containerID;
    }

    /**
     * @see #detachKeys
     */
    @CheckForNull
    public String getDetachKeys() {
        return detachKeys;
    }

    /**
     * @see #pid
     * @deprecated use {@link #getPidLong()}
     */
    @CheckForNull
    @Deprecated
    public Integer getPid() {
        return pid != null ? pid.intValue() : null;
    }

    /**
     * @see #pid
     */
    @CheckForNull
    public Long getPidLong() {
        return pid;
    }

    @Override
    public String toString() {
        return "InspectExecResponse{" +
                "id='" + id + '\'' +
                ", openStdin=" + openStdin +
                ", openStderr=" + openStderr +
                ", openStdout=" + openStdout +
                ", running=" + running +
                ", canRemove=" + canRemove +
                ", exitCode=" + exitCode +
                ", processConfig=" + processConfig +
                ", container=" + container +
                ", containerID='" + containerID + '\'' +
                ", detachKeys='" + detachKeys + '\'' +
                ", pid=" + pid +
                '}';
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
            return "ProcessConfig{" +
                    "arguments=" + arguments +
                    ", entryPoint='" + entryPoint + '\'' +
                    ", privileged=" + privileged +
                    ", tty=" + tty +
                    ", user='" + user + '\'' +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Container {

        @JsonProperty("NetworkSettings")
        private NetworkSettings networkSettings;

        /**
         * @since {@link RemoteApiVersion#VERSION_1_21}
         */
        public NetworkSettings getNetworkSettings() {
            return networkSettings;
        }
    }
}
