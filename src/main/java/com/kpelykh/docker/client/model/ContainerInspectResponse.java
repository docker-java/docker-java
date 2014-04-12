package com.kpelykh.docker.client.model;


import org.codehaus.jackson.annotate.JsonProperty;

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

    @JsonProperty("HostConfig")
    private HostConfig hostConfig;

    /**
     * http://blog.docker.io/2014/03/docker-0-9-introducing-execution-drivers-and-libcontainer/
     */
    @JsonProperty("ExecDriver")
    private String execDriver;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public ContainerConfig getConfig() {
        return config;
    }

    public void setConfig(ContainerConfig config) {
        this.config = config;
    }

    public ContainerState getState() {
        return state;
    }

    public void setState(ContainerState state) {
        this.state = state;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public NetworkSettings getNetworkSettings() {
        return networkSettings;
    }

    public void setNetworkSettings(NetworkSettings networkSettings) {
        this.networkSettings = networkSettings;
    }

    public String getSysInitPath() {
        return sysInitPath;
    }

    public void setSysInitPath(String sysInitPath) {
        this.sysInitPath = sysInitPath;
    }

    public String getResolvConfPath() {
        return resolvConfPath;
    }

    public void setResolvConfPath(String resolvConfPath) {
        this.resolvConfPath = resolvConfPath;
    }

    public Map<String, String> getVolumes() {
        return volumes;
    }

    public void setVolumes(Map<String, String> volumes) {
        this.volumes = volumes;
    }

    public Map<String, String> getVolumesRW() {
        return volumesRW;
    }

    public void setVolumesRW(Map<String, String> volumesRW) {
        this.volumesRW = volumesRW;
    }

    public String getHostnamePath() {
        return hostnamePath;
    }

    public void setHostnamePath(String hostnamePath) {
        this.hostnamePath = hostnamePath;
    }

    public String getHostsPath() {
        return hostsPath;
    }

    public void setHostsPath(String hostsPath) {
        this.hostsPath = hostsPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public HostConfig getHostConfig() {
        return hostConfig;
    }

    public void setHostConfig(HostConfig hostConfig) {
        this.hostConfig = hostConfig;
    }

    public class NetworkSettings {

        @JsonProperty("IPAddress") public String ipAddress;
        @JsonProperty("IPPrefixLen") public int ipPrefixLen;
        @JsonProperty("Gateway") public String gateway;
        @JsonProperty("Bridge") public String bridge;
        @JsonProperty("PortMapping") public Map<String,Map<String, String>> portMapping;
        @JsonProperty("Ports") public Ports ports;

        @Override
        public String toString() {
            return "NetworkSettings{" +
                    "ports=" + ports +
                    ", portMapping=" + portMapping +
                    ", bridge='" + bridge + '\'' +
                    ", gateway='" + gateway + '\'' +
                    ", ipPrefixLen=" + ipPrefixLen +
                    ", ipAddress='" + ipAddress + '\'' +
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
