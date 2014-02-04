package com.kpelykh.docker.client.model;


import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Arrays;
import java.util.Map;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
public class ContainerInspectResponse {

    @JsonProperty("ID")
    private String id;

    @JsonProperty("Created")
    private String created;

    @JsonProperty("Path")
    private String path;

    @JsonProperty("Args")
    private String[] args;

    @JsonProperty("Config")
    public ContainerConfig config;

    @JsonProperty("State")
    private ContainerState state;

    @JsonProperty("Image")
    private String image;

    @JsonProperty("NetworkSettings")
    private NetworkSettings networkSettings;

    @JsonProperty("SysInitPath")
    private String sysInitPath;

    @JsonProperty("ResolvConfPath")
    private String resolvConfPath;

    @JsonProperty("Volumes")
    private Map<String, String> volumes;

    @JsonProperty("VolumesRW")
    private Map<String, String> volumesRW;

    @JsonProperty("HostnamePath")
    private String hostnamePath;

    @JsonProperty("HostsPath")
    private String hostsPath;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Driver")
    private String driver;

    public String getDriver() {
        return driver;
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

    public String getId() {
        return id;
    }

    public String getCreated() {
        return created;
    }

    public String getPath() {
        return path;
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

    public String getImage() {
        return image;
    }

    public NetworkSettings getNetworkSettings() {
        return networkSettings;
    }

    public String getSysInitPath() {
        return sysInitPath;
    }

    public String getResolvConfPath() {
        return resolvConfPath;
    }

    public Map<String, String> getVolumes() {
        return volumes;
    }

    public Map<String, String> getVolumesRW() {
        return volumesRW;
    }

    @Override
    public String toString() {
        return "ContainerInspectResponse{" +
                "id='" + id + '\'' +
                ", created='" + created + '\'' +
                ", path='" + path + '\'' +
                ", args=" + Arrays.toString(args) +
                ", config=" + config +
                ", state=" + state +
                ", image='" + image + '\'' +
                ", networkSettings=" + networkSettings +
                ", sysInitPath='" + sysInitPath + '\'' +
                ", resolvConfPath='" + resolvConfPath + '\'' +
                ", volumes=" + volumes +
                ", volumesRW=" + volumesRW +
                ", hostnamePath='" + hostnamePath + '\'' +
                ", hostsPath='" + hostsPath + '\'' +
                ", name='" + name + '\'' +
                ", driver='" + driver + '\'' +
                '}';
    }

    public class NetworkSettings {

        @JsonProperty("IPAddress") public String ipAddress;
        @JsonProperty("IPPrefixLen") public int ipPrefixLen;
        @JsonProperty("Gateway") public String gateway;
        @JsonProperty("Bridge") public String bridge;
        // Deprecated - can we remove?
        @JsonProperty("PortMapping") public Map<String,Map<String, String>> portMapping;
        // FIXME Is this the right type? -BJE
        @JsonProperty("Ports") public Map<String, String> ports;

        @Override
        public String toString() {
            return "NetworkSettings{" +
                    "ipAddress='" + ipAddress + '\'' +
                    ", ipPrefixLen=" + ipPrefixLen +
                    ", gateway='" + gateway + '\'' +
                    ", bridge='" + bridge + '\'' +
                    ", ports=" + ports +
                    '}';
        }
    }

    public class ContainerState {

        @JsonProperty("Running") public boolean running;
        @JsonProperty("Pid") public int pid;
        @JsonProperty("ExitCode") public int exitCode;
        @JsonProperty("StartedAt") public String startedAt;
        @JsonProperty("Ghost") public boolean ghost;
        @JsonProperty("FinishedAt") private String finishedAt;

        @Override
        public String toString() {
            return "ContainerState{" +
                    "running=" + running +
                    ", pid=" + pid +
                    ", exitCode=" + exitCode +
                    ", startedAt='" + startedAt + '\'' +
                    ", ghost=" + ghost +
                    ", finishedAt='" + finishedAt + '\'' +
                    '}';
        }
    }

}
