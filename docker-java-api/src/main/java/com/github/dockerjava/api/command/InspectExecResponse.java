package com.github.dockerjava.api.command;

import java.util.List;

import com.github.dockerjava.api.annotation.FieldName;
import com.github.dockerjava.api.model.NetworkSettings;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;

@EqualsAndHashCode
@ToString
public class InspectExecResponse {
    @FieldName("ID")
    private String id;

    @FieldName("OpenStdin")
    private Boolean openStdin;

    @FieldName("OpenStderr")
    private Boolean openStderr;

    @FieldName("OpenStdout")
    private Boolean openStdout;

    @FieldName("Running")
    private Boolean running;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_22}
     */
    @FieldName("CanRemove")
    private Boolean canRemove;

    @FieldName("ExitCode")
    private Long exitCode;

    @FieldName("ProcessConfig")
    private ProcessConfig processConfig;

    /**
     * @deprecated @since {@link RemoteApiVersion#VERSION_1_22}
     */
    @Deprecated
    @FieldName("Container")
    private Container container;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_22}
     */
    @FieldName("ContainerID")
    private String containerID;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_22}
     */
    @FieldName("DetachKeys")
    private String detachKeys;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_25}
     */
    @FieldName("Pid")
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

    @EqualsAndHashCode
    @ToString
    public class ProcessConfig {

        @FieldName("arguments")
        private List<String> arguments;

        @FieldName("entrypoint")
        private String entryPoint;

        @FieldName("privileged")
        private Boolean privileged;

        @FieldName("tty")
        private Boolean tty;

        @FieldName("user")
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
    }

    public class Container {

        @FieldName("NetworkSettings")
        private NetworkSettings networkSettings;

        /**
         * @since {@link RemoteApiVersion#VERSION_1_21}
         */
        public NetworkSettings getNetworkSettings() {
            return networkSettings;
        }
    }
}
