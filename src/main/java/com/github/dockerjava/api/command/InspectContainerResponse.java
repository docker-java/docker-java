package com.github.dockerjava.api.command;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.model.ContainerConfig;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.VolumeBind;
import com.github.dockerjava.api.model.VolumeBinds;
import com.github.dockerjava.api.model.VolumeRW;
import com.github.dockerjava.api.model.VolumesRW;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Image")
    private String imageId;

    @JsonProperty("MountLabel")
    private String mountLabel;

    @JsonProperty("Name")
    private String name;

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

    public String getId() {
        return id;
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

    @JsonIgnore
    public VolumeRW[] getVolumesRW() {
        return volumesRW == null ? null : volumesRW.getVolumesRW();
    }

    public String getHostnamePath() {
        return hostnamePath;
    }

    public String getHostsPath() {
        return hostsPath;
    }

    public String getName() {
        return name;
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

    public List<String> getExecIds() {
        return execIds;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class NetworkSettings {

        @JsonProperty("IPAddress")
        private String ipAddress;

        @JsonProperty("IPPrefixLen")
        private Integer ipPrefixLen;

        @JsonProperty("Gateway")
        private String gateway;

        @JsonProperty("Bridge")
        private String bridge;

        @JsonProperty("PortMapping")
        private Map<String, Map<String, String>> portMapping;

        @JsonProperty("Ports")
        private Ports ports;

        public String getIpAddress() {
            return ipAddress;
        }

        public Integer getIpPrefixLen() {
            return ipPrefixLen;
        }

        public String getGateway() {
            return gateway;
        }

        public String getBridge() {
            return bridge;
        }

        public Map<String, Map<String, String>> getPortMapping() {
            return portMapping;
        }

        public Ports getPorts() {
            return ports;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class ContainerState {

        @JsonProperty("Running")
        private Boolean running;

        @JsonProperty("Paused")
        private Boolean paused;

        @JsonProperty("Pid")
        private Integer pid;

        @JsonProperty("ExitCode")
        private Integer exitCode;

        @JsonProperty("StartedAt")
        private String startedAt;

        @JsonProperty("FinishedAt")
        private String finishedAt;

        public Boolean isRunning() {
            return running;
        }

        public Boolean isPaused() {
            return paused;
        }

        public Integer getPid() {
            return pid;
        }

        public Integer getExitCode() {
            return exitCode;
        }

        public String getStartedAt() {
            return startedAt;
        }

        public String getFinishedAt() {
            return finishedAt;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

}
