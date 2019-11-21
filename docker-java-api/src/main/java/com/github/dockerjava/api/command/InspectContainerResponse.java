package com.github.dockerjava.api.command;

import com.github.dockerjava.api.annotation.FieldName;
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

    @FieldName("Args")
    private String[] args;

    @FieldName("Config")
    private ContainerConfig config;

    @FieldName("Created")
    private String created;

    @FieldName("Driver")
    private String driver;

    @FieldName("ExecDriver")
    private String execDriver;

    @FieldName("HostConfig")
    private HostConfig hostConfig;

    @FieldName("HostnamePath")
    private String hostnamePath;

    @FieldName("HostsPath")
    private String hostsPath;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_17}
     */
    @FieldName("LogPath")
    private String logPath;

    @FieldName("Id")
    private String id;

    @FieldName("SizeRootFs")
    private Integer sizeRootFs;

    @FieldName("Image")
    private String imageId;

    @FieldName("MountLabel")
    private String mountLabel;

    @FieldName("Name")
    private String name;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_17}
     */
    @FieldName("RestartCount")
    private Integer restartCount;

    @FieldName("NetworkSettings")
    private NetworkSettings networkSettings;

    @FieldName("Path")
    private String path;

    @FieldName("ProcessLabel")
    private String processLabel;

    @FieldName("ResolvConfPath")
    private String resolvConfPath;

    @FieldName("ExecIDs")
    private List<String> execIds;

    @FieldName("State")
    private ContainerState state;

    @FieldName("Volumes")
    private VolumeBinds volumes;

    @FieldName("VolumesRW")
    private VolumesRW volumesRW;

    @FieldName("Node")
    private Node node;

    @FieldName("Mounts")
    private List<Mount> mounts;

    @FieldName("GraphDriver")
    private GraphDriver graphDriver;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_30}
     */
    @FieldName("Platform")
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

    public VolumeBind[] getVolumes() {
        return volumes == null ? null : volumes.getBinds();
    }

    /**
     * @deprecated As of {@link RemoteApiVersion#VERSION_1_20} use {@link #getMounts()} instead
     */
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
        @FieldName("Status")
        private String status;

        /**
         * @since < {@link RemoteApiVersion#VERSION_1_16}
         */
        @CheckForNull
        @FieldName("Running")
        private Boolean running;

        /**
         * @since {@link RemoteApiVersion#VERSION_1_17}
         */
        @CheckForNull
        @FieldName("Paused")
        private Boolean paused;

        /**
         * @since {@link RemoteApiVersion#VERSION_1_17}
         */
        @CheckForNull
        @FieldName("Restarting")
        private Boolean restarting;

        /**
         * @since {@link RemoteApiVersion#VERSION_1_17}
         */
        @CheckForNull
        @FieldName("OOMKilled")
        private Boolean oomKilled;

        /**
         * <a href="https://github.com/docker/docker/pull/18127">Unclear</a>
         *
         * @since {@link RemoteApiVersion#UNKNOWN_VERSION}
         */
        @CheckForNull
        @FieldName("Dead")
        private Boolean dead;

        /**
         * @since < {@link RemoteApiVersion#VERSION_1_16}
         */
        @CheckForNull
        @FieldName("Pid")
        private Long pid;

        /**
         * @since < {@link RemoteApiVersion#VERSION_1_16}
         */
        @CheckForNull
        @FieldName("ExitCode")
        private Long exitCode;

        /**
         * @since {@link RemoteApiVersion#VERSION_1_17}
         */
        @CheckForNull
        @FieldName("Error")
        private String error;

        /**
         * @since < {@link RemoteApiVersion#VERSION_1_16}
         */
        @CheckForNull
        @FieldName("StartedAt")
        private String startedAt;

        /**
         * @since {@link RemoteApiVersion#VERSION_1_17}
         */
        @CheckForNull
        @FieldName("FinishedAt")
        private String finishedAt;


        /**
         * @since Docker version 1.12
         */
        @FieldName("Health")
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
        @FieldName("Name")
        private String name;

        /**
         * @since {@link RemoteApiVersion#VERSION_1_20}
         */
        @CheckForNull
        @FieldName("Source")
        private String source;

        /**
         * @since {@link RemoteApiVersion#VERSION_1_20}
         */
        @CheckForNull
        @FieldName("Destination")
        private Volume destination;

        /**
         * @since {@link RemoteApiVersion#VERSION_1_20}
         */
        @CheckForNull
        @FieldName("Driver")
        private String driver;

        /**
         * @since {@link RemoteApiVersion#VERSION_1_20}
         */
        @CheckForNull
        @FieldName("Mode")
        private String mode;

        /**
         * @since {@link RemoteApiVersion#VERSION_1_20}
         */
        @CheckForNull
        @FieldName("RW")
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

        @FieldName("ID")
        private String id;

        @FieldName("IP")
        private String ip;

        @FieldName("Addr")
        private String addr;

        @FieldName("Name")
        private String name;

        @FieldName("Cpus")
        private Integer cpus;

        @FieldName("Memory")
        private Long memory;

        @FieldName("Labels")
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
