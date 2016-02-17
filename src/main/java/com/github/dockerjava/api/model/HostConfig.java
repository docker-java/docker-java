package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.CheckForNull;
import java.util.List;

/**
 * Used in `/containers/create`.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class HostConfig {

    @JsonProperty("Binds")
    private Binds binds;

    @JsonProperty("BlkioWeight")
    private Integer blkioWeight;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("BlkioWeightDevice")
    private List<Object> blkioWeightDevice;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("BlkioDeviceReadBps")
    private List<Object> blkioDeviceReadBps;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("BlkioDeviceReadIOps")
    private List<Object> blkioDeviceReadIOps;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("BlkioDeviceWriteBps")
    private List<Object> blkioDeviceWriteBps;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("BlkioDeviceWriteIOps")
    private List<Object> blkioDeviceWriteIOps;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_20}
     */
    @JsonProperty("MemorySwappiness")
    private Integer memorySwappiness;

    @JsonProperty("CapAdd")
    private Capability[] capAdd;

    @JsonProperty("CapDrop")
    private Capability[] capDrop;

    @JsonProperty("ContainerIDFile")
    private String containerIDFile;

    @JsonProperty("CpuPeriod")
    private Integer cpuPeriod;

    @JsonProperty("CpuShares")
    private Integer cpuShares;

    /**
     * @since ~{@link RemoteApiVersion#VERSION_1_20}
     */
    @JsonProperty("CpuQuota")
    private Integer cpuQuota;

    @JsonProperty("CpusetCpus")
    private String cpusetCpus;

    @JsonProperty("CpusetMems")
    private String cpusetMems;

    @JsonProperty("Devices")
    private Device[] devices;

    @JsonProperty("Dns")
    private String[] dns;

    @JsonProperty("DnsSearch")
    private String[] dnsSearch;

    @JsonProperty("ExtraHosts")
    private String[] extraHosts;

    @JsonProperty("Links")
    private Links links;

    @JsonProperty("LogConfig")
    private LogConfig logConfig;

    @JsonProperty("LxcConf")
    private LxcConf[] lxcConf;

    @JsonProperty("Memory")
    private Long memory;

    @JsonProperty("MemorySwap")
    private Long memorySwap;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_21}
     */
    @JsonProperty("MemoryReservation")
    private Long memoryReservation;

     /**
     * @since {@link RemoteApiVersion#VERSION_1_21}
     */
    @JsonProperty("KernelMemory")
    private Long kernelMemory;

    @JsonProperty("NetworkMode")
    private String networkMode;

    @JsonProperty("OomKillDisable")
    private Boolean oomKillDisable;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("OomScoreAdj")
    private Boolean oomScoreAdj;

    @JsonProperty("PortBindings")
    private Ports portBindings;

    @JsonProperty("Privileged")
    private Boolean privileged;

    @JsonProperty("PublishAllPorts")
    private Boolean publishAllPorts;

    @JsonProperty("ReadonlyRootfs")
    private Boolean readonlyRootfs;

    @JsonProperty("RestartPolicy")
    private RestartPolicy restartPolicy;

    @JsonProperty("Ulimits")
    private Ulimit[] ulimits;

    @JsonProperty("VolumesFrom")
    private VolumesFrom[] volumesFrom;

    @JsonProperty("PidMode")
    private String pidMode;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_20}
     */
    @JsonProperty("SecurityOpt")
    private List<String> securityOpts;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_20}
     */
    @JsonProperty("CgroupParent")
    private String cgroupParent;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_21}
     */
    @JsonProperty("VolumeDriver")
    private String volumeDriver;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("ShmSize")
    private String shmSize;


    @JsonIgnore
    public Bind[] getBinds() {
        return (binds == null) ? new Bind[0] : binds.getBinds();
    }

    public Integer getBlkioWeight() {
        return blkioWeight;
    }

    public Capability[] getCapAdd() {
        return capAdd;
    }

    public Capability[] getCapDrop() {
        return capDrop;
    }

    public String getContainerIDFile() {
        return containerIDFile;
    }

    public Integer getCpuPeriod() {
        return cpuPeriod;
    }

    public Integer getCpuShares() {
        return cpuShares;
    }

    public String getCpusetCpus() {
        return cpusetCpus;
    }

    public String getCpusetMems() {
        return cpusetMems;
    }

    public Device[] getDevices() {
        return devices;
    }

    public String[] getDns() {
        return dns;
    }

    public String[] getDnsSearch() {
        return dnsSearch;
    }

    public String[] getExtraHosts() {
        return extraHosts;
    }

    @JsonIgnore
    public Link[] getLinks() {
        return (links == null) ? new Link[0] : links.getLinks();
    }

    @JsonIgnore
    public LogConfig getLogConfig() {
        return (logConfig == null) ? new LogConfig() : logConfig;
    }

    public LxcConf[] getLxcConf() {
        return lxcConf;
    }

    public Long getMemory() {
        return memory;
    }

    public Long getMemorySwap() {
        return memorySwap;
    }

    public String getNetworkMode() {
        return networkMode;
    }

    public Ports getPortBindings() {
        return portBindings;
    }

    public RestartPolicy getRestartPolicy() {
        return restartPolicy;
    }

    public Ulimit[] getUlimits() {
        return ulimits;
    }

    public VolumesFrom[] getVolumesFrom() {
        return volumesFrom;
    }

    public Boolean isOomKillDisable() {
        return oomKillDisable;
    }

    public Boolean isPrivileged() {
        return privileged;
    }

    public Boolean isPublishAllPorts() {
        return publishAllPorts;
    }

    public Boolean isReadonlyRootfs() {
        return readonlyRootfs;
    }

    @CheckForNull
    public String getPidMode() {
        return pidMode;
    }

    /**
     * @see #blkioDeviceReadBps
     */
    @CheckForNull
    public List<Object> getBlkioDeviceReadBps() {
        return blkioDeviceReadBps;
    }

    /**
     * @see #blkioDeviceReadIOps
     */
    @CheckForNull
    public List<Object> getBlkioDeviceReadIOps() {
        return blkioDeviceReadIOps;
    }

    /**
     * @see #blkioDeviceWriteBps
     */
    @CheckForNull
    public List<Object> getBlkioDeviceWriteBps() {
        return blkioDeviceWriteBps;
    }

    /**
     * @see #blkioDeviceWriteIOps
     */
    @CheckForNull
    public List<Object> getBlkioDeviceWriteIOps() {
        return blkioDeviceWriteIOps;
    }

    /**
     * @see #blkioWeightDevice
     */
    @CheckForNull
    public List<Object> getBlkioWeightDevice() {
        return blkioWeightDevice;
    }

    /**
     * @see #oomScoreAdj
     */
    @CheckForNull
    public Boolean getOomScoreAdj() {
        return oomScoreAdj;
    }

    /**
     * @see #cpuQuota
     */
    @CheckForNull
    public Integer getCpuQuota() {
        return cpuQuota;
    }

    /**
     * @see #kernelMemory
     */
    @CheckForNull
    public Long getKernelMemory() {
        return kernelMemory;
    }

    /**
     * @see #memoryReservation
     */
    @CheckForNull
    public Long getMemoryReservation() {
        return memoryReservation;
    }

    /**
     * @see #memorySwappiness
     */
    @CheckForNull
    public Integer getMemorySwappiness() {
        return memorySwappiness;
    }

    /**
     * @see #oomKillDisable
     */
    @CheckForNull
    public Boolean getOomKillDisable() {
        return oomKillDisable;
    }

    /**
     * @see #securityOpts
     */
    @CheckForNull
    public List<String> getSecurityOpts() {
        return securityOpts;
    }

    /**
     * @see #cgroupParent
     */
    @CheckForNull
    public String getCgroupParent() {
        return cgroupParent;
    }

    /**
     * @see #shmSize
     */
    @CheckForNull
    public String getShmSize() {
        return shmSize;
    }

    /**
     * @see #volumeDriver
     */
    @CheckForNull
    public String getVolumeDriver() {
        return volumeDriver;
    }

    @JsonIgnore
    public void setBinds(Bind... binds) {
        this.binds = new Binds(binds);
    }

    public void setBlkioWeight(Integer blkioWeight) {
        this.blkioWeight = blkioWeight;
    }

    public void setCpuPeriod(Integer cpuPeriod) {
        this.cpuPeriod = cpuPeriod;
    }

    public void setCpuShares(Integer cpuShares) {
        this.cpuShares = cpuShares;
    }

    public void setCpusetCpus(String cpusetCpus) {
        this.cpusetCpus = cpusetCpus;
    }

    public void setCpusetMems(String cpusetMems) {
        this.cpusetMems = cpusetMems;
    }

    public void setCapAdd(Capability[] capAdd) {
        this.capAdd = capAdd;
    }

    public void setCapDrop(Capability[] capDrop) {
        this.capDrop = capDrop;
    }

    public void setContainerIDFile(String containerIDFile) {
        this.containerIDFile = containerIDFile;
    }

    public void setDevices(Device[] devices) {
        this.devices = devices;
    }

    public void setDns(String[] dns) {
        this.dns = dns;
    }

    public void setDnsSearch(String[] dnsSearch) {
        this.dnsSearch = dnsSearch;
    }

    public void setExtraHosts(String[] extraHosts) {
        this.extraHosts = extraHosts;
    }

    @JsonIgnore
    public void setLinks(Link... links) {
        this.links = new Links(links);
    }

    @JsonIgnore
    public void setLogConfig(LogConfig logConfig) {
        this.logConfig = logConfig;
    }

    public void setLxcConf(LxcConf[] lxcConf) {
        this.lxcConf = lxcConf;
    }

    public void setMemory(Long memory) {
        this.memory = memory;
    }

    public void setMemorySwap(Long memorySwap) {
        this.memorySwap = memorySwap;
    }

    public void setNetworkMode(String networkMode) {
        this.networkMode = networkMode;
    }

    public void setOomKillDisable(Boolean oomKillDisable) {
        this.oomKillDisable = oomKillDisable;
    }

    public void setPortBindings(Ports portBindings) {
        this.portBindings = portBindings;
    }

    public void setPrivileged(Boolean privileged) {
        this.privileged = privileged;
    }

    public void setPublishAllPorts(Boolean publishAllPorts) {
        this.publishAllPorts = publishAllPorts;
    }

    public void setReadonlyRootfs(Boolean readonlyRootfs) {
        this.readonlyRootfs = readonlyRootfs;
    }

    public void setRestartPolicy(RestartPolicy restartPolicy) {
        this.restartPolicy = restartPolicy;
    }

    public void setUlimits(Ulimit[] ulimits) {
        this.ulimits = ulimits;
    }

    public void setVolumesFrom(VolumesFrom[] volumesFrom) {
        this.volumesFrom = volumesFrom;
    }

    public void setPidMode(String pidMode) {
        this.pidMode = pidMode;
    }

    public HostConfig setBlkioDeviceReadBps(List<Object> blkioDeviceReadBps) {
        this.blkioDeviceReadBps = blkioDeviceReadBps;
        return this;
    }

    public HostConfig setBlkioDeviceReadIOps(List<Object> blkioDeviceReadIOps) {
        this.blkioDeviceReadIOps = blkioDeviceReadIOps;
        return this;
    }

    public HostConfig setBlkioDeviceWriteBps(List<Object> blkioDeviceWriteBps) {
        this.blkioDeviceWriteBps = blkioDeviceWriteBps;
        return this;
    }

    public HostConfig setBlkioDeviceWriteIOps(List<Object> blkioDeviceWriteIOps) {
        this.blkioDeviceWriteIOps = blkioDeviceWriteIOps;
        return this;
    }

    public HostConfig setBlkioWeightDevice(List<Object> blkioWeightDevice) {
        this.blkioWeightDevice = blkioWeightDevice;
        return this;
    }

    public HostConfig setCpuQuota(Integer cpuQuota) {
        this.cpuQuota = cpuQuota;
        return this;
    }

    public HostConfig setKernelMemory(Long kernelMemory) {
        this.kernelMemory = kernelMemory;
        return this;
    }

    public HostConfig setMemoryReservation(Long memoryReservation) {
        this.memoryReservation = memoryReservation;
        return this;
    }

    public HostConfig setMemorySwappiness(Integer memorySwappiness) {
        this.memorySwappiness = memorySwappiness;
        return this;
    }

    public HostConfig setOomScoreAdj(Boolean oomScoreAdj) {
        this.oomScoreAdj = oomScoreAdj;
        return this;
    }

    public HostConfig setSecurityOpts(List<String> securityOpts) {
        this.securityOpts = securityOpts;
        return this;
    }

    public HostConfig setCgroupParent(String cgroupParent) {
        this.cgroupParent = cgroupParent;
        return this;
    }

    public HostConfig setShmSize(String shmSize) {
        this.shmSize = shmSize;
        return this;
    }

    public HostConfig setVolumeDriver(String volumeDriver) {
        this.volumeDriver = volumeDriver;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
