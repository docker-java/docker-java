package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
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

    @JsonIgnore
    public void setLinks(Link... links) {
        this.links = new Links(links);
    }

    // auto-generated builder setters
    /**
     * @see #binds
     */
    public HostConfig withBinds(Binds binds) {
        this.binds = binds;
        return this;
    }

    /**
     * @see #blkioDeviceReadBps
     */
    public HostConfig withBlkioDeviceReadBps(List<Object> blkioDeviceReadBps) {
        this.blkioDeviceReadBps = blkioDeviceReadBps;
        return this;
    }

    /**
     * @see #blkioDeviceReadIOps
     */
    public HostConfig withBlkioDeviceReadIOps(List<Object> blkioDeviceReadIOps) {
        this.blkioDeviceReadIOps = blkioDeviceReadIOps;
        return this;
    }

    /**
     * @see #blkioDeviceWriteBps
     */
    public HostConfig withBlkioDeviceWriteBps(List<Object> blkioDeviceWriteBps) {
        this.blkioDeviceWriteBps = blkioDeviceWriteBps;
        return this;
    }

    /**
     * @see #blkioDeviceWriteIOps
     */
    public HostConfig withBlkioDeviceWriteIOps(List<Object> blkioDeviceWriteIOps) {
        this.blkioDeviceWriteIOps = blkioDeviceWriteIOps;
        return this;
    }

    /**
     * @see #blkioWeight
     */
    public HostConfig withBlkioWeight(Integer blkioWeight) {
        this.blkioWeight = blkioWeight;
        return this;
    }

    /**
     * @see #blkioWeightDevice
     */
    public HostConfig withBlkioWeightDevice(List<Object> blkioWeightDevice) {
        this.blkioWeightDevice = blkioWeightDevice;
        return this;
    }

    /**
     * @see #capAdd
     */
    public HostConfig withCapAdd(Capability[] capAdd) {
        this.capAdd = capAdd;
        return this;
    }

    /**
     * @see #capDrop
     */
    public HostConfig withCapDrop(Capability[] capDrop) {
        this.capDrop = capDrop;
        return this;
    }

    /**
     * @see #cgroupParent
     */
    public HostConfig withCgroupParent(String cgroupParent) {
        this.cgroupParent = cgroupParent;
        return this;
    }

    /**
     * @see #containerIDFile
     */
    public HostConfig withContainerIDFile(String containerIDFile) {
        this.containerIDFile = containerIDFile;
        return this;
    }

    /**
     * @see #cpuPeriod
     */
    public HostConfig withCpuPeriod(Integer cpuPeriod) {
        this.cpuPeriod = cpuPeriod;
        return this;
    }

    /**
     * @see #cpuQuota
     */
    public HostConfig withCpuQuota(Integer cpuQuota) {
        this.cpuQuota = cpuQuota;
        return this;
    }

    /**
     * @see #cpusetCpus
     */
    public HostConfig withCpusetCpus(String cpusetCpus) {
        this.cpusetCpus = cpusetCpus;
        return this;
    }

    /**
     * @see #cpusetMems
     */
    public HostConfig withCpusetMems(String cpusetMems) {
        this.cpusetMems = cpusetMems;
        return this;
    }

    /**
     * @see #cpuShares
     */
    public HostConfig withCpuShares(Integer cpuShares) {
        this.cpuShares = cpuShares;
        return this;
    }

    /**
     * @see #devices
     */
    public HostConfig withDevices(Device[] devices) {
        this.devices = devices;
        return this;
    }

    /**
     * @see #dns
     */
    public HostConfig withDns(String[] dns) {
        this.dns = dns;
        return this;
    }

    /**
     * @see #dnsSearch
     */
    public HostConfig withDnsSearch(String[] dnsSearch) {
        this.dnsSearch = dnsSearch;
        return this;
    }

    /**
     * @see #extraHosts
     */
    public HostConfig withExtraHosts(String[] extraHosts) {
        this.extraHosts = extraHosts;
        return this;
    }

    /**
     * @see #kernelMemory
     */
    public HostConfig withKernelMemory(Long kernelMemory) {
        this.kernelMemory = kernelMemory;
        return this;
    }

    /**
     * @see #links
     */
    public HostConfig withLinks(Links links) {
        this.links = links;
        return this;
    }

    /**
     * @see #logConfig
     */
    public HostConfig withLogConfig(LogConfig logConfig) {
        this.logConfig = logConfig;
        return this;
    }

    /**
     * @see #lxcConf
     */
    public HostConfig withLxcConf(LxcConf[] lxcConf) {
        this.lxcConf = lxcConf;
        return this;
    }

    /**
     * @see #memory
     */
    public HostConfig withMemory(Long memory) {
        this.memory = memory;
        return this;
    }

    /**
     * @see #memoryReservation
     */
    public HostConfig withMemoryReservation(Long memoryReservation) {
        this.memoryReservation = memoryReservation;
        return this;
    }

    /**
     * @see #memorySwap
     */
    public HostConfig withMemorySwap(Long memorySwap) {
        this.memorySwap = memorySwap;
        return this;
    }

    /**
     * @see #memorySwappiness
     */
    public HostConfig withMemorySwappiness(Integer memorySwappiness) {
        this.memorySwappiness = memorySwappiness;
        return this;
    }

    /**
     * @see #networkMode
     */
    public HostConfig withNetworkMode(String networkMode) {
        this.networkMode = networkMode;
        return this;
    }

    /**
     * @see #oomKillDisable
     */
    public HostConfig withOomKillDisable(Boolean oomKillDisable) {
        this.oomKillDisable = oomKillDisable;
        return this;
    }

    /**
     * @see #oomScoreAdj
     */
    public HostConfig withOomScoreAdj(Boolean oomScoreAdj) {
        this.oomScoreAdj = oomScoreAdj;
        return this;
    }

    /**
     * @see #pidMode
     */
    public HostConfig withPidMode(String pidMode) {
        this.pidMode = pidMode;
        return this;
    }

    /**
     * @see #portBindings
     */
    public HostConfig withPortBindings(Ports portBindings) {
        this.portBindings = portBindings;
        return this;
    }

    /**
     * @see #privileged
     */
    public HostConfig withPrivileged(Boolean privileged) {
        this.privileged = privileged;
        return this;
    }

    /**
     * @see #publishAllPorts
     */
    public HostConfig withPublishAllPorts(Boolean publishAllPorts) {
        this.publishAllPorts = publishAllPorts;
        return this;
    }

    /**
     * @see #readonlyRootfs
     */
    public HostConfig withReadonlyRootfs(Boolean readonlyRootfs) {
        this.readonlyRootfs = readonlyRootfs;
        return this;
    }

    /**
     * @see #restartPolicy
     */
    public HostConfig withRestartPolicy(RestartPolicy restartPolicy) {
        this.restartPolicy = restartPolicy;
        return this;
    }

    /**
     * @see #securityOpts
     */
    public HostConfig withSecurityOpts(List<String> securityOpts) {
        this.securityOpts = securityOpts;
        return this;
    }

    /**
     * @see #shmSize
     */
    public HostConfig withShmSize(String shmSize) {
        this.shmSize = shmSize;
        return this;
    }

    /**
     * @see #ulimits
     */
    public HostConfig withUlimits(Ulimit[] ulimits) {
        this.ulimits = ulimits;
        return this;
    }

    /**
     * @see #volumeDriver
     */
    public HostConfig withVolumeDriver(String volumeDriver) {
        this.volumeDriver = volumeDriver;
        return this;
    }

    /**
     * @see #volumesFrom
     */
    public HostConfig withVolumesFrom(VolumesFrom[] volumesFrom) {
        this.volumesFrom = volumesFrom;
        return this;
    }
    // end of auto-generated

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        HostConfig that = (HostConfig) o;

        return new EqualsBuilder()
                .append(binds, that.binds)
                .append(blkioWeight, that.blkioWeight)
                .append(blkioWeightDevice, that.blkioWeightDevice)
                .append(blkioDeviceReadBps, that.blkioDeviceReadBps)
                .append(blkioDeviceReadIOps, that.blkioDeviceReadIOps)
                .append(blkioDeviceWriteBps, that.blkioDeviceWriteBps)
                .append(blkioDeviceWriteIOps, that.blkioDeviceWriteIOps)
                .append(memorySwappiness, that.memorySwappiness)
                .append(capAdd, that.capAdd)
                .append(capDrop, that.capDrop)
                .append(containerIDFile, that.containerIDFile)
                .append(cpuPeriod, that.cpuPeriod)
                .append(cpuShares, that.cpuShares)
                .append(cpuQuota, that.cpuQuota)
                .append(cpusetCpus, that.cpusetCpus)
                .append(cpusetMems, that.cpusetMems)
                .append(devices, that.devices)
                .append(dns, that.dns)
                .append(dnsSearch, that.dnsSearch)
                .append(extraHosts, that.extraHosts)
                .append(links, that.links)
                .append(logConfig, that.logConfig)
                .append(lxcConf, that.lxcConf)
                .append(memory, that.memory)
                .append(memorySwap, that.memorySwap)
                .append(memoryReservation, that.memoryReservation)
                .append(kernelMemory, that.kernelMemory)
                .append(networkMode, that.networkMode)
                .append(oomKillDisable, that.oomKillDisable)
                .append(oomScoreAdj, that.oomScoreAdj)
                .append(portBindings, that.portBindings)
                .append(privileged, that.privileged)
                .append(publishAllPorts, that.publishAllPorts)
                .append(readonlyRootfs, that.readonlyRootfs)
                .append(restartPolicy, that.restartPolicy)
                .append(ulimits, that.ulimits)
                .append(volumesFrom, that.volumesFrom)
                .append(pidMode, that.pidMode)
                .append(securityOpts, that.securityOpts)
                .append(cgroupParent, that.cgroupParent)
                .append(volumeDriver, that.volumeDriver)
                .append(shmSize, that.shmSize)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(binds)
                .append(blkioWeight)
                .append(blkioWeightDevice)
                .append(blkioDeviceReadBps)
                .append(blkioDeviceReadIOps)
                .append(blkioDeviceWriteBps)
                .append(blkioDeviceWriteIOps)
                .append(memorySwappiness)
                .append(capAdd)
                .append(capDrop)
                .append(containerIDFile)
                .append(cpuPeriod)
                .append(cpuShares)
                .append(cpuQuota)
                .append(cpusetCpus)
                .append(cpusetMems)
                .append(devices)
                .append(dns)
                .append(dnsSearch)
                .append(extraHosts)
                .append(links)
                .append(logConfig)
                .append(lxcConf)
                .append(memory)
                .append(memorySwap)
                .append(memoryReservation)
                .append(kernelMemory)
                .append(networkMode)
                .append(oomKillDisable)
                .append(oomScoreAdj)
                .append(portBindings)
                .append(privileged)
                .append(publishAllPorts)
                .append(readonlyRootfs)
                .append(restartPolicy)
                .append(ulimits)
                .append(volumesFrom)
                .append(pidMode)
                .append(securityOpts)
                .append(cgroupParent)
                .append(volumeDriver)
                .append(shmSize)
                .toHashCode();
    }
}
