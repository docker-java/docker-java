package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.CheckForNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HostConfig {

    @JsonProperty("Binds")
    private Binds binds;

    @JsonProperty("Links")
    private Links links;

    @JsonProperty("LxcConf")
    private LxcConf[] lxcConf;

    @JsonProperty("LogConfig")
    private LogConfig logConfig;

    @JsonProperty("PortBindings")
    private Ports portBindings;

    @JsonProperty("PublishAllPorts")
    private boolean publishAllPorts;

    @JsonProperty("Privileged")
    private boolean privileged;

    @JsonProperty("ReadonlyRootfs")
    private boolean readonlyRootfs;

    @JsonProperty("Dns")
    private String[] dns;

    @JsonProperty("DnsSearch")
    private String[] dnsSearch;

    @JsonProperty("VolumesFrom")
    private VolumesFrom[] volumesFrom;

    @JsonProperty("ContainerIDFile")
    private String containerIDFile;

    @JsonProperty("CapAdd")
    private Capability[] capAdd;

    @JsonProperty("CapDrop")
    private Capability[] capDrop;

    @JsonProperty("RestartPolicy")
    private RestartPolicy restartPolicy;

    @JsonProperty("NetworkMode")
    private String networkMode;

    @JsonProperty("Devices")
    private Device[] devices;

    @JsonProperty("ExtraHosts")
    private String[] extraHosts;

    @JsonProperty("Ulimits")
    private Ulimit[] ulimits;

    @JsonProperty("Memory")
    private long memoryLimit = 0;

    @JsonProperty("MemorySwap")
    private long memorySwap = 0;

    @JsonProperty("CpuShares")
    private int cpuShares = 0;

    @JsonProperty("PidMode")
    private String pidMode;

    public HostConfig() {
    }

    public HostConfig(Bind[] binds, Link[] links, LxcConf[] lxcConf, LogConfig logConfig, Ports portBindings,
            boolean publishAllPorts, boolean privileged, boolean readonlyRootfs, String[] dns, String[] dnsSearch,
            VolumesFrom[] volumesFrom, String containerIDFile, Capability[] capAdd, Capability[] capDrop,
            RestartPolicy restartPolicy, String networkMode, Device[] devices, String[] extraHosts, Ulimit[] ulimits,
            String pidMode) {
        this.binds = new Binds(binds);
        this.links = new Links(links);
        this.lxcConf = lxcConf;
        this.logConfig = logConfig;
        this.portBindings = portBindings;
        this.publishAllPorts = publishAllPorts;
        this.privileged = privileged;
        this.readonlyRootfs = readonlyRootfs;
        this.dns = dns;
        this.dnsSearch = dnsSearch;
        this.volumesFrom = volumesFrom;
        this.containerIDFile = containerIDFile;
        this.capAdd = capAdd;
        this.capDrop = capDrop;
        this.restartPolicy = restartPolicy;
        this.networkMode = networkMode;
        this.devices = devices;
        this.extraHosts = extraHosts;
        this.ulimits = ulimits;
        this.pidMode = pidMode;
    }

    @JsonIgnore
    public Bind[] getBinds() {
        return (binds == null) ? new Bind[0] : binds.getBinds();
    }

    public LxcConf[] getLxcConf() {
        return lxcConf;
    }

    @JsonIgnore
    public LogConfig getLogConfig() {
        return (logConfig == null) ? new LogConfig() : logConfig;
    }

    public Ports getPortBindings() {
        return portBindings;
    }

    public boolean isPublishAllPorts() {
        return publishAllPorts;
    }

    public boolean isPrivileged() {
        return privileged;
    }

    public boolean isReadonlyRootfs() {
        return readonlyRootfs;
    }

    public String[] getDns() {
        return dns;
    }

    public VolumesFrom[] getVolumesFrom() {
        return volumesFrom;
    }

    public String getContainerIDFile() {
        return containerIDFile;
    }

    public String[] getDnsSearch() {
        return dnsSearch;
    }

    @JsonIgnore
    public Link[] getLinks() {
        return (links == null) ? new Link[0] : links.getLinks();
    }

    public String getNetworkMode() {
        return networkMode;
    }

    public Device[] getDevices() {
        return devices;
    }

    public String[] getExtraHosts() {
        return extraHosts;
    }

    public RestartPolicy getRestartPolicy() {
        return restartPolicy;
    }

    public Capability[] getCapAdd() {
        return capAdd;
    }

    public Capability[] getCapDrop() {
        return capDrop;
    }

    public Ulimit[] getUlimits() {
        return ulimits;
    }

    public long getMemoryLimit() {
        return memoryLimit;
    }

    public long getMemorySwap() {
        return memorySwap;
    }

    public int getCpuShares() {
        return cpuShares;
    }

    @CheckForNull
    public String getPidMode() {
        return pidMode;
    }

    @JsonIgnore
    public void setBinds(Bind... binds) {
        this.binds = new Binds(binds);
    }

    @JsonIgnore
    public void setLinks(Link... links) {
        this.links = new Links(links);
    }

    public void setLxcConf(LxcConf[] lxcConf) {
        this.lxcConf = lxcConf;
    }

    @JsonIgnore
    public void setLogConfig(LogConfig logConfig) {
        this.logConfig = logConfig;
    }

    public void setPortBindings(Ports portBindings) {
        this.portBindings = portBindings;
    }

    public void setPublishAllPorts(boolean publishAllPorts) {
        this.publishAllPorts = publishAllPorts;
    }

    public void setPrivileged(boolean privileged) {
        this.privileged = privileged;
    }

    public void setReadonlyRootfs(boolean readonlyRootfs) {
        this.readonlyRootfs = readonlyRootfs;
    }

    public void setDns(String[] dns) {
        this.dns = dns;
    }

    public void setDnsSearch(String[] dnsSearch) {
        this.dnsSearch = dnsSearch;
    }

    public void setVolumesFrom(VolumesFrom[] volumesFrom) {
        this.volumesFrom = volumesFrom;
    }

    public void setContainerIDFile(String containerIDFile) {
        this.containerIDFile = containerIDFile;
    }

    public void setCapAdd(Capability[] capAdd) {
        this.capAdd = capAdd;
    }

    public void setCapDrop(Capability[] capDrop) {
        this.capDrop = capDrop;
    }

    public void setRestartPolicy(RestartPolicy restartPolicy) {
        this.restartPolicy = restartPolicy;
    }

    public void setNetworkMode(String networkMode) {
        this.networkMode = networkMode;
    }

    public void setDevices(Device[] devices) {
        this.devices = devices;
    }

    public void setExtraHosts(String[] extraHosts) {
        this.extraHosts = extraHosts;
    }

    public void setUlimits(Ulimit[] ulimits) {
        this.ulimits = ulimits;
    }

    public void setPidMode(String pidMode) {
        this.pidMode = pidMode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
