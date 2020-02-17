package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.model.ContainerConfig;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.NetworkSettings;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.api.model.VolumeBind;
import com.github.dockerjava.api.model.VolumeBinds;
import com.github.dockerjava.api.model.VolumeRW;
import com.github.dockerjava.api.model.VolumesRW;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@EqualsAndHashCode
@ToString
public class InspectContainerResponse {

    @JsonProperty("Args")
    private String[] args;

    @JsonProperty("Config")
    private ContainerConfig config;

    @JsonProperty("Created")
    private String created;

    @JsonProperty("Driver")
    private String driver;

    @JsonProperty("ExecDriver")
    private String execDriver;

    @JsonProperty("HostConfig")
    private HostConfig hostConfig;

    @JsonProperty("HostnamePath")
    private String hostnamePath;

    @JsonProperty("HostsPath")
    private String hostsPath;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_17}
     */
    @JsonProperty("LogPath")
    private String logPath;

    @JsonProperty("Id")
    private String id;

    @JsonProperty("SizeRootFs")
    private Integer sizeRootFs;

    @JsonProperty("Image")
    private String imageId;

    @JsonProperty("MountLabel")
    private String mountLabel;

    @JsonProperty("Name")
    private String name;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_17}
     */
    @JsonProperty("RestartCount")
    private Integer restartCount;

    @JsonProperty("NetworkSettings")
    private NetworkSettings networkSettings;

    @JsonProperty("Path")
    private String path;

    @JsonProperty("ProcessLabel")
    private String processLabel;

    @JsonProperty("ResolvConfPath")
    private String resolvConfPath;

    @JsonProperty("ExecIDs")
    private List<String> execIds;

    @JsonProperty("State")
    private ContainerState state;

    @JsonProperty("Volumes")
    private VolumeBinds volumes;

    @JsonProperty("VolumesRW")
    private VolumesRW volumesRW;

    @JsonProperty("Node")
    private Node node;

    @JsonProperty("Mounts")
    private List<Mount> mounts;

    @JsonProperty("GraphDriver")
    private GraphDriver graphDriver;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_30}
     */
    @JsonProperty("Platform")
    private String platform;

    public String getId() {
        return id;
    }

    public Integer getSizeRootFs() {
        return sizeRootFs;
    }

    public String getCreated() {
        return created;
    }

    public String getPath() {
        return path;
    }

    public String getProcessLabel() {
        return processLabel;
    }

    public String[] getArgs() {
        return args;
    }

    public ContainerConfig getConfig() {
        return config;
    }

    public ContainerState getState() {
        return state;
    }

    public String getImageId() {
        return imageId;
    }

    public NetworkSettings getNetworkSettings() {
        return networkSettings;
    }

    public String getResolvConfPath() {
        return resolvConfPath;
    }

    @JsonIgnore
    public VolumeBind[] getVolumes() {
        return volumes == null ? null : volumes.getBinds();
    }

    /**
     * @deprecated As of {@link RemoteApiVersion#VERSION_1_20} use {@link #getMounts()} instead
     */
    @JsonIgnore
    @Deprecated
    @CheckForNull
    public VolumeRW[] getVolumesRW() {
        return volumesRW == null ? null : volumesRW.getVolumesRW();
    }

    public String getHostnamePath() {
        return hostnamePath;
    }

    public String getHostsPath() {
        return hostsPath;
    }

    @CheckForNull
    public String getLogPath() {
        return logPath;
    }

    public String getName() {
        return name;
    }

    public Integer getRestartCount() {
        return restartCount;
    }

    public String getDriver() {
        return driver;
    }

    public HostConfig getHostConfig() {
        return hostConfig;
    }

    public String getExecDriver() {
        return execDriver;
    }

    public String getMountLabel() {
        return mountLabel;
    }

    /**
     * @since {@link RemoteApiVersion#VERSION_1_20}
     */
    @CheckForNull
    public List<Mount> getMounts() {
        return mounts;
    }

    public List<String> getExecIds() {
        return execIds;
    }

    /**
     * Get the underlying swarm node info. This property does only contains a value in swarm-classic
     * @return The underlying swarm-classic node info
     * @CheckForNull
     */
    public Node getNode() {
        return node;
    }

    /**
     * @see #graphDriver
     */
    @CheckForNull
    public GraphDriver getGraphDriver() {
        return graphDriver;
    }

    /**
     * @see #platform
     */
    @CheckForNull
    public String getPlatform() {
        return platform;
    }

    @EqualsAndHashCode
    @ToString
    public class ContainerState {

        /**
         * @since {@link RemoteApiVersion#VERSION_1_20}
         */
        @CheckForNull
        @JsonProperty("Status")
        private String status;

        /**
         * @since < {@link RemoteApiVersion#VERSION_1_16}
         */
        @CheckForNull
        @JsonProperty("Running")
        private Boolean running;

        /**
         * @since {@link RemoteApiVersion#VERSION_1_17}
         */
        @CheckForNull
        @JsonProperty("Paused")
        private Boolean paused;

        /**
         * @since {@link RemoteApiVersion#VERSION_1_17}
         */
        @CheckForNull
        @JsonProperty("Restarting")
        private Boolean restarting;

        /**
         * @since {@link RemoteApiVersion#VERSION_1_17}
         */
        @CheckForNull
        @JsonProperty("OOMKilled")
        private Boolean oomKilled;

        /**
         * <a href="https://github.com/docker/docker/pull/18127">Unclear</a>
         *
         * @since {@link RemoteApiVersion#UNKNOWN_VERSION}
         */
        @CheckForNull
        @JsonProperty("Dead")
        private Boolean dead;

        /**
         * @since < {@link RemoteApiVersion#VERSION_1_16}
         */
        @CheckForNull
        @JsonProperty("Pid")
        private Long pid;

        /**
         * @since < {@link RemoteApiVersion#VERSION_1_16}
         */
        @CheckForNull
        @JsonProperty("ExitCode")
        private Long exitCode;

        /**
         * @since {@link RemoteApiVersion#VERSION_1_17}
         */
        @CheckForNull
        @JsonProperty("Error")
        private String error;

        /**
         * @since < {@link RemoteApiVersion#VERSION_1_16}
         */
        @CheckForNull
        @JsonProperty("StartedAt")
        private String startedAt;

        /**
         * @since {@link RemoteApiVersion#VERSION_1_17}
         */
        @CheckForNull
        @JsonProperty("FinishedAt")
        private String finishedAt;


        /**
         * @since Docker version 1.12
         */
        @JsonProperty("Health")
        private HealthState health;

        /**
         * See {@link #status}
         */
        @CheckForNull
        public String getStatus() {
            return status;
        }

        /**
         * See {@link #running}
         */
        @CheckForNull
        public Boolean getRunning() {
            return running;
        }

        /**
         * See {@link #paused}
         */
        @CheckForNull
        public Boolean getPaused() {
            return paused;
        }

        /**
         * See {@link #restarting}
         */
        @CheckForNull
        public Boolean getRestarting() {
            return restarting;
        }

        /**
         * See {@link #oomKilled}
         */
        @CheckForNull
        public Boolean getOOMKilled() {
            return oomKilled;
        }

        /**
         * See {@link #dead}
         */
        @CheckForNull
        public Boolean getDead() {
            return dead;
        }

        /**
         * See {@link #pid}
         *
         * @deprecated use {@link #getPidLong()}
         */
        @Deprecated
        @CheckForNull
        public Integer getPid() {
            return pid != null ? pid.intValue() : null;
        }

        /**
         * See {@link #pid}
         */
        @CheckForNull
        public Long getPidLong() {
            return pid;
        }

        /**
         * See {@link #exitCode}
         *
         * @deprecated use {@link #getExitCodeLong()}
         */
        @Deprecated
        @CheckForNull
        public Integer getExitCode() {
            return exitCode != null ? exitCode.intValue() : null;
        }

        /**
         * See {@link #exitCode}
         */
        @CheckForNull
        public Long getExitCodeLong() {
            return exitCode;
        }

        /**
         * See {@link #error}
         */
        @CheckForNull
        public String getError() {
            return error;
        }

        /**
         * See {@link #startedAt}
         */
        @CheckForNull
        public String getStartedAt() {
            return startedAt;
        }

        /**
         * See {@link #finishedAt}
         */
        @CheckForNull
        public String getFinishedAt() {
            return finishedAt;
        }

        public HealthState getHealth() {
            return health;
        }
    }

    @EqualsAndHashCode
    @ToString
    public static class Mount {

        /**
         * @since {@link RemoteApiVersion#VERSION_1_20}
         */
        @CheckForNull
        @JsonProperty("Name")
        private String name;

        /**
         * @since {@link RemoteApiVersion#VERSION_1_20}
         */
        @CheckForNull
        @JsonProperty("Source")
        private String source;

        /**
         * @since {@link RemoteApiVersion#VERSION_1_20}
         */
        @CheckForNull
        @JsonProperty("Destination")
        private Volume destination;

        /**
         * @since {@link RemoteApiVersion#VERSION_1_20}
         */
        @CheckForNull
        @JsonProperty("Driver")
        private String driver;

        /**
         * @since {@link RemoteApiVersion#VERSION_1_20}
         */
        @CheckForNull
        @JsonProperty("Mode")
        private String mode;

        /**
         * @since {@link RemoteApiVersion#VERSION_1_20}
         */
        @CheckForNull
        @JsonProperty("RW")
        private Boolean rw;

        @CheckForNull
        public String getName() {
            return name;
        }

        @CheckForNull
        public String getSource() {
            return source;
        }

        @CheckForNull
        public Volume getDestination() {
            return destination;
        }

        @CheckForNull
        public String getDriver() {
            return driver;
        }

        @CheckForNull
        public String getMode() {
            return mode;
        }

        @CheckForNull
        public Boolean getRW() {
            return rw;
        }

        /**
         * @see #destination
         */
        public Mount withDestination(Volume destination) {
            this.destination = destination;
            return this;
        }

        /**
         * @see #driver
         */
        public Mount withDriver(String driver) {
            this.driver = driver;
            return this;
        }

        /**
         * @see #mode
         */
        public Mount withMode(String mode) {
            this.mode = mode;
            return this;
        }

        /**
         * @see #name
         */
        public Mount withName(String name) {
            this.name = name;
            return this;
        }

        /**
         * @see #rw
         */
        public Mount withRw(Boolean rw) {
            this.rw = rw;
            return this;
        }

        /**
         * @see #source
         */
        public Mount withSource(String source) {
            this.source = source;
            return this;
        }
    }

    @EqualsAndHashCode
    @ToString
    public class Node {

        @JsonProperty("ID")
        private String id;

        @JsonProperty("IP")
        private String ip;

        @JsonProperty("Addr")
        private String addr;

        @JsonProperty("Name")
        private String name;

        @JsonProperty("Cpus")
        private Integer cpus;

        @JsonProperty("Memory")
        private Long memory;

        @JsonProperty("Labels")
        private Map<String, String> labels;

        public String getId() {
            return id;
        }

        public String getIp() {
            return ip;
        }

        public String getAddr() {
            return addr;
        }

        public String getName() {
            return name;
        }

        public Integer getCpus() {
            return cpus;
        }

        public Long getMemory() {
            return memory;
        }

        public Map<String, String> getLabels() {
            return labels;
        }
    }
}
