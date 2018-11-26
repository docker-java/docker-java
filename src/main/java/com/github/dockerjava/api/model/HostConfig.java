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
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Used in `/containers/create`, and in inspect container.
 * TODO exclude usage for 2 different models.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class HostConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final List<String> PREDEFINED_NETWORKS = Arrays.asList("bridge", "host", "none");

    @JsonProperty("Binds")
    private Binds binds;

    /**
     * @since 1.19
     */
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

    /**
     * @since 1.19
     */
    @JsonProperty("CpuPeriod")
    private Long cpuPeriod;

    @JsonProperty("CpuRealtimePeriod")
    private Long cpuRealtimePeriod;

    @JsonProperty("CpuRealtimeRuntime")
    private Long cpuRealtimeRuntime;

    @JsonProperty("CpuShares")
    private Integer cpuShares;

    /**
     * @since ~{@link RemoteApiVersion#VERSION_1_20}
     */
    @JsonProperty("CpuQuota")
    private Long cpuQuota;

    @JsonProperty("CpusetCpus")
    private String cpusetCpus;

    /**
     * @since 1.19
     */
    @JsonProperty("CpusetMems")
    private String cpusetMems;

    @JsonProperty("Devices")
    private Device[] devices;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_25}
     */
    @JsonProperty("DiskQuota")
    private Long diskQuota;

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

    @JsonProperty("NanoCPUs")
    private Long nanoCPUs;

    @JsonProperty("CpuPercent")
    private Long cpuPercent;

    @JsonProperty("NetworkMode")
    private String networkMode;

    @JsonProperty("OomKillDisable")
    private Boolean oomKillDisable;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_25}
     */
    @JsonProperty("AutoRemove")
    private Boolean autoRemove;

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

    /**
     * @since {@link RemoteApiVersion#VERSION_1_24}
     */
    @JsonProperty("StorageOpt")
    private Map<String, String> storageOpt;

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
    private Long shmSize;

    /**
     * @since ~{@link RemoteApiVersion#VERSION_1_23}
     */
    @JsonProperty("PidsLimit")
    private Long pidsLimit;

    /**
     * @since ~{@link RemoteApiVersion#VERSION_1_30}
     */
    @JsonProperty("Runtime")
    private String runtime;

    /**
     * @since ~{@link RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("Tmpfs")
    private Map<String, String> tmpFs;

    /**
     * @since ~{@link RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("Isolation")
    private String isolation;

    public static HostConfig newHostConfig() {
        return new HostConfig();
    }

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

    public Long getCpuPeriod() {
        return cpuPeriod;
    }

    public Long getCpuRealtimePeriod() {
        return cpuRealtimePeriod;
    }

    public Long getCpuRealtimeRuntime() {
        return cpuRealtimeRuntime;
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

    public Long getDiskQuota() {
        return diskQuota;
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

    @CheckForNull
    public String getPidMode() {
        return pidMode;
    }

    /**
     * @see #isolation
     */
    @CheckForNull
    public String getIsolation() {
        return isolation;
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
    public Long getCpuQuota() {
        return cpuQuota;
    }

    /**
     * @see #kernelMemory
     */
    @CheckForNull
    public Long getKernelMemory() {
        return kernelMemory;
    }

    @CheckForNull
    public Long getNanoCPUs() {
        return nanoCPUs;
    }

    @CheckForNull
    public Long getCpuPercent() {
        return cpuPercent;
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
     * @see #autoRemove
     */
    @CheckForNull
    public Boolean getAutoRemove() {
        return autoRemove;
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
    public Long getShmSize() {
        return shmSize;
    }

    /**
     * @see #volumeDriver
     */
    @CheckForNull
    public String getVolumeDriver() {
        return volumeDriver;
    }

    /**
     * @see #pidsLimit
     */
    @CheckForNull
    public Long getPidsLimit() {
        return pidsLimit;
    }

    /**
     * @see #tmpFs
     */
    @CheckForNull
    public Map<String, String> getTmpFs() {
        return tmpFs;
    }

    /**
     * Parse the network mode as specified at
     * {@see https://github.com/docker/engine-api/blob/master/types/container/hostconfig_unix.go}
     */
    @JsonIgnore
    public boolean isUserDefinedNetwork() {
        return networkMode != null && !PREDEFINED_NETWORKS.contains(networkMode) && !networkMode.startsWith("container:");
    }

    public String getRuntime() {
        return runtime;
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

    public HostConfig withBinds(Bind... binds) {
        checkNotNull(binds, "binds was not specified");
        setBinds(binds);
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
     * Add linux <a href="http://man7.org/linux/man-pages/man7/capabilities.7.html">kernel capability</a> to the container. For example:
     * adding {@link Capability#MKNOD} allows the container to create special files using the 'mknod' command.
     */
    public HostConfig withCapAdd(Capability... capAdd) {
        this.capAdd = capAdd;
        return this;
    }

    /**
     * @see #capDrop
     */
    public HostConfig withCapDrop(Capability... capDrop) {
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
    public HostConfig withCpuPeriod(Long cpuPeriod) {
        this.cpuPeriod = cpuPeriod;
        return this;
    }

    public HostConfig withCpuRealtimePeriod(Long cpuRealtimePeriod) {
        this.cpuRealtimePeriod = cpuRealtimePeriod;
        return this;
    }

    public HostConfig withCpuRealtimeRuntime(Long cpuRealtimeRuntime) {
        this.cpuRealtimeRuntime = cpuRealtimeRuntime;
        return this;
    }

    /**
     * @see #cpuQuota
     */
    public HostConfig withCpuQuota(Long cpuQuota) {
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

    public HostConfig withDevices(List<Device> devices) {
        checkNotNull(devices, "devices was not specified");
        return withDevices(devices.toArray(new Device[devices.size()]));
    }

    /**
     * @see #devices
     */
    public HostConfig withDevices(Device... devices) {
        this.devices = devices;
        return this;
    }

    /**
     * @see #diskQuota
     */
    public HostConfig withDiskQuota(Long diskQuota) {
        this.diskQuota = diskQuota;
        return this;
    }

    /**
     * @see #dns
     */
    public HostConfig withDns(String... dns) {
        this.dns = dns;
        return this;
    }

    public HostConfig withDns(List<String> dns) {
        checkNotNull(dns, "dns was not specified");
        return withDns(dns.toArray(new String[dns.size()]));
    }

    /**
     * @see #dnsSearch
     */
    public HostConfig withDnsSearch(String... dnsSearch) {
        this.dnsSearch = dnsSearch;
        return this;
    }

    public HostConfig withDnsSearch(List<String> dnsSearch) {
        checkNotNull(dnsSearch, "dnsSearch was not specified");
        return withDnsSearch(dnsSearch.toArray(new String[0]));
    }

    /**
     * @see #extraHosts
     */
    public HostConfig withExtraHosts(String... extraHosts) {
        this.extraHosts = extraHosts;
        return this;
    }

    public HostConfig withExtraHosts(List<String> extraHosts) {
        checkNotNull(extraHosts, "extraHosts was not specified");
        return withExtraHosts(extraHosts.toArray(new String[extraHosts.size()]));
    }

    /**
     * @see #kernelMemory
     */
    public HostConfig withKernelMemory(Long kernelMemory) {
        this.kernelMemory = kernelMemory;
        return this;
    }

    public HostConfig withNanoCPUs(Long nanoCPUs) {
        this.nanoCPUs = nanoCPUs;
        return this;
    }

    public HostConfig withCpuPercent(Long cpuPercent) {
        this.cpuPercent = cpuPercent;
        return this;
    }

    /**
     * @see #links
     */
    public HostConfig withLinks(Links links) {
        this.links = links;
        return this;
    }

    public HostConfig withLinks(List<Link> links) {
        checkNotNull(links, "links was not specified");
        setLinks(links.toArray(new Link[links.size()]));
        return this;
    }

    public HostConfig withLinks(Link... links) {
        checkNotNull(links, "links was not specified");
        setLinks(links);
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

    public HostConfig withLxcConf(List<LxcConf> lxcConf) {
        checkNotNull(lxcConf, "lxcConf was not specified");
        return withLxcConf(lxcConf.toArray(new LxcConf[0]));
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
     * Set the Network mode for the container
     * <ul>
     * <li>'bridge': creates a new network stack for the container on the docker bridge</li>
     * <li>'none': no networking for this container</li>
     * <li>'container:<name|id>': reuses another container network stack</li>
     * <li>'host': use the host network stack inside the container. Note: the host mode gives the container full access to local system
     * services such as D-bus and is therefore considered insecure.</li>
     * </ul>
     *
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
     * @see #autoRemove
     */
    public HostConfig withAutoRemove(Boolean autoRemove) {
        this.autoRemove = autoRemove;
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
    public HostConfig withPortBindings(PortBinding... portBindings) {
        checkNotNull(portBindings, "portBindings was not specified");
        return withPortBindings(new Ports(portBindings));
    }

    public HostConfig withPortBindings(List<PortBinding> portBindings) {
        checkNotNull(portBindings, "portBindings was not specified");
        return withPortBindings(portBindings.toArray(new PortBinding[0]));
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
    @CheckForNull
    public Boolean getPrivileged() {
        return privileged;
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
    @CheckForNull
    public Boolean getPublishAllPorts() {
        return publishAllPorts;
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
    @CheckForNull
    public Boolean getReadonlyRootfs() {
        return readonlyRootfs;
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
     * @see #storageOpt
     */
    public Map<String, String> getStorageOpt() {
        return storageOpt;
    }

    /**
     * @see #storageOpt
     */
    public HostConfig withStorageOpt(Map<String, String> storageOpt) {
        this.storageOpt = storageOpt;
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
    public HostConfig withShmSize(Long shmSize) {
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

    public HostConfig withUlimits(List<Ulimit> ulimits) {
        checkNotNull(ulimits, "no ulimits was specified");
        return withUlimits(ulimits.toArray(new Ulimit[ulimits.size()]));
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
    public HostConfig withVolumesFrom(VolumesFrom... volumesFrom) {
        this.volumesFrom = volumesFrom;
        return this;
    }

    public HostConfig withVolumesFrom(List<VolumesFrom> volumesFrom) {
        checkNotNull(volumesFrom, "volumesFrom was not specified");
        return withVolumesFrom(volumesFrom.toArray(new VolumesFrom[volumesFrom.size()]));
    }


    /**
     * @see #pidsLimit
     */
    public HostConfig withPidsLimit(Long pidsLimit) {
        this.pidsLimit = pidsLimit;
        return this;
    }

    public HostConfig withRuntime(String runtime) {
        this.runtime = runtime;
        return this;
    }

    /**
     * @see #tmpFs
     */
    public HostConfig withTmpFs(Map<String, String> tmpFs) {
        this.tmpFs = tmpFs;
        return this;
    }

    /**
     * @see #isolation
     */
    public HostConfig withIsolation(String isolation) {
        this.isolation = isolation;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
