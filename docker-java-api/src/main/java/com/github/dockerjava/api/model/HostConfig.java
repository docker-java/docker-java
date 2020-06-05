package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * Used in `/containers/create`, and in inspect container.
 * TODO exclude usage for 2 different models.
 */
@EqualsAndHashCode
@ToString
public class HostConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final List<String> PREDEFINED_NETWORKS = Arrays.asList("bridge", "host", "none");

    public static HostConfig newHostConfig() {
        return new HostConfig();
    }

    @JsonProperty("Binds")
    private Binds binds;

    @JsonProperty("BlkioWeight")
    private Integer blkioWeight;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("BlkioWeightDevice")
    private List<BlkioWeightDevice> blkioWeightDevice;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("BlkioDeviceReadBps")
    private List<BlkioRateDevice> blkioDeviceReadBps;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("BlkioDeviceWriteBps")
    private List<BlkioRateDevice> blkioDeviceWriteBps;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("BlkioDeviceReadIOps")
    private List<BlkioRateDevice> blkioDeviceReadIOps;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("BlkioDeviceWriteIOps")
    private List<BlkioRateDevice> blkioDeviceWriteIOps;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_20}
     */
    @JsonProperty("MemorySwappiness")
    private Long memorySwappiness;

    @JsonProperty("NanoCpus")
    private Long nanoCPUs;

    @JsonProperty("CapAdd")
    private Capability[] capAdd;

    @JsonProperty("CapDrop")
    private Capability[] capDrop;

    @JsonProperty("ContainerIDFile")
    private String containerIDFile;

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

    @JsonProperty("CpusetMems")
    private String cpusetMems;

    @JsonProperty("Devices")
    private Device[] devices;

    @JsonProperty("DeviceCgroupRules")
    private List<String> deviceCgroupRules;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_40}
     */
    @JsonProperty("DeviceRequests")
    private List<DeviceRequest> deviceRequests;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_25}
     */
    @JsonProperty("DiskQuota")
    private Long diskQuota;

    @JsonProperty("Dns")
    private String[] dns;

    @JsonProperty("DnsOptions")
    private List<String> dnsOptions;

    @JsonProperty("DnsSearch")
    private String[] dnsSearch;

    @JsonProperty("ExtraHosts")
    private String[] extraHosts;

    @JsonProperty("GroupAdd")
    private List<String> groupAdd;

    @JsonProperty("IpcMode")
    private String ipcMode;

    @JsonProperty("Cgroup")
    private String cgroup;

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

    @JsonProperty("Init")
    private Boolean init;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_25}
     */
    @JsonProperty("AutoRemove")
    private Boolean autoRemove;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("OomScoreAdj")
    private Integer oomScoreAdj;

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

    @JsonProperty("CpuCount")
    private Long cpuCount;

    @JsonProperty("CpuPercent")
    private Long cpuPercent;

    @JsonProperty("IOMaximumIOps")
    private Long ioMaximumIOps;

    @JsonProperty("IOMaximumBandwidth")
    private Long ioMaximumBandwidth;

    @JsonProperty("VolumesFrom")
    private VolumesFrom[] volumesFrom;

    @JsonProperty("Mounts")
    private List<Mount> mounts;

    @JsonProperty("PidMode")
    private String pidMode;

    @JsonProperty("Isolation")
    private Isolation isolation;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_20}
     */
    @JsonProperty("SecurityOpt")
    private List<String> securityOpts;

    @JsonProperty("StorageOpt")
    private Map<String, String> storageOpt;

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

    @JsonProperty("UTSMode")
    private String utSMode;

    @JsonProperty("UsernsMode")
    private String usernsMode;

    @JsonProperty("Sysctls")
    private Map<String, String> sysctls;

    @JsonProperty("ConsoleSize")
    private List<Integer> consoleSize;

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
     * @see #blkioDeviceReadBps
     */
    @CheckForNull
    public List<BlkioRateDevice> getBlkioDeviceReadBps() {
        return blkioDeviceReadBps;
    }

    /**
     * @see #blkioDeviceReadIOps
     */
    @CheckForNull
    public List<BlkioRateDevice> getBlkioDeviceReadIOps() {
        return blkioDeviceReadIOps;
    }

    /**
     * @see #blkioDeviceWriteBps
     */
    @CheckForNull
    public List<BlkioRateDevice> getBlkioDeviceWriteBps() {
        return blkioDeviceWriteBps;
    }

    /**
     * @see #blkioDeviceWriteIOps
     */
    @CheckForNull
    public List<BlkioRateDevice> getBlkioDeviceWriteIOps() {
        return blkioDeviceWriteIOps;
    }

    /**
     * @see #blkioWeightDevice
     */
    @CheckForNull
    public List<BlkioWeightDevice> getBlkioWeightDevice() {
        return blkioWeightDevice;
    }

    /**
     * @see #oomScoreAdj
     */
    @CheckForNull
    public Integer getOomScoreAdj() {
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
    public Long getMemorySwappiness() {
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
        requireNonNull(binds, "binds was not specified");
        setBinds(binds);
        return this;
    }

    public HostConfig withBinds(List<Bind> binds) {
        requireNonNull(binds, "binds was not specified");
        return withBinds(binds.toArray(new Bind[binds.size()]));
    }

    /**
     * @see #blkioDeviceReadBps
     */
    public HostConfig withBlkioDeviceReadBps(List<BlkioRateDevice> blkioDeviceReadBps) {
        this.blkioDeviceReadBps = blkioDeviceReadBps;
        return this;
    }

    /**
     * @see #blkioDeviceReadIOps
     */
    public HostConfig withBlkioDeviceReadIOps(List<BlkioRateDevice> blkioDeviceReadIOps) {
        this.blkioDeviceReadIOps = blkioDeviceReadIOps;
        return this;
    }

    /**
     * @see #blkioDeviceWriteBps
     */
    public HostConfig withBlkioDeviceWriteBps(List<BlkioRateDevice> blkioDeviceWriteBps) {
        this.blkioDeviceWriteBps = blkioDeviceWriteBps;
        return this;
    }

    /**
     * @see #blkioDeviceWriteIOps
     */
    public HostConfig withBlkioDeviceWriteIOps(List<BlkioRateDevice> blkioDeviceWriteIOps) {
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
    public HostConfig withBlkioWeightDevice(List<BlkioWeightDevice> blkioWeightDevice) {
        this.blkioWeightDevice = blkioWeightDevice;
        return this;
    }

    /**
     * @see #capAdd
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

    /**
     * @see #devices
     */
    public HostConfig withDevices(Device... devices) {
        this.devices = devices;
        return this;
    }

    public HostConfig withDevices(List<Device> devices) {
        requireNonNull(devices, "devices was not specified");
        return withDevices(devices.toArray(new Device[0]));
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
        requireNonNull(dns, "dns was not specified");
        return withDns(dns.toArray(new String[0]));
    }

    /**
     * @see #dnsSearch
     */
    public HostConfig withDnsSearch(String... dnsSearch) {
        this.dnsSearch = dnsSearch;
        return this;
    }

    public HostConfig withDnsSearch(List<String> dnsSearch) {
        requireNonNull(dnsSearch, "dnsSearch was not specified");
        return withDnsSearch(dnsSearch.toArray(new String[0]));
    }

    /**
     * @see #extraHosts
     */
    public HostConfig withExtraHosts(String... extraHosts) {
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

    public HostConfig withLinks(Link... links) {
        requireNonNull(links, "links was not specified");
        setLinks(links);
        return this;
    }

    public HostConfig withLinks(List<Link> links) {
        requireNonNull(links, "links was not specified");
        return withLinks(links.toArray(new Link[0]));
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
    public HostConfig withMemorySwappiness(Long memorySwappiness) {
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
     */
    public HostConfig withNetworkMode(String networkMode) {
        this.networkMode = networkMode;
        return this;
    }

    /**
     * @see #oomKillDisable
     * @since 1.19
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
    public HostConfig withOomScoreAdj(Integer oomScoreAdj) {
        this.oomScoreAdj = oomScoreAdj;
        return this;
    }

    /**
     * Set the PID (Process) Namespace mode for the container, 'host': use the host's PID namespace inside the container
     *
     * @see #pidMode
     */
    public HostConfig withPidMode(String pidMode) {
        this.pidMode = pidMode;
        return this;
    }

    /**
     * Add one or more {@link PortBinding}s. This corresponds to the <code>--publish</code> (<code>-p</code>) option of the
     * <code>docker run</code> CLI command.
     */
    public HostConfig withPortBindings(Ports portBindings) {
        this.portBindings = portBindings;
        return this;
    }

    public HostConfig withPortBindings(PortBinding... portBindings) {
        requireNonNull(portBindings, "portBindings was not specified");
        withPortBindings(new Ports(portBindings));
        return this;
    }

    public HostConfig withPortBindings(List<PortBinding> portBindings) {
        requireNonNull(portBindings, "portBindings was not specified");
        return withPortBindings(portBindings.toArray(new PortBinding[0]));
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
     * Set custom {@link RestartPolicy} for the container. Defaults to {@link RestartPolicy#noRestart()}
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
        requireNonNull(ulimits, "no ulimits was specified");
        return withUlimits(ulimits.toArray(new Ulimit[0]));
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
        requireNonNull(volumesFrom, "volumesFrom was not specified");
        return withVolumesFrom(volumesFrom.toArray(new VolumesFrom[0]));
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

    @CheckForNull
    public List<String> getDeviceCgroupRules() {
        return deviceCgroupRules;
    }

    public HostConfig withDeviceCgroupRules(List<String> deviceCgroupRules) {
        this.deviceCgroupRules = deviceCgroupRules;
        return this;
    }

    @CheckForNull
    public List<DeviceRequest> getDeviceRequests() {
        return deviceRequests;
    }

    public HostConfig withDeviceRequests(List<DeviceRequest> deviceRequests) {
        this.deviceRequests = deviceRequests;
        return this;
    }

    @CheckForNull
    public Long getNanoCPUs() {
        return nanoCPUs;
    }

    public HostConfig withNanoCPUs(Long nanoCPUs) {
        this.nanoCPUs = nanoCPUs;
        return this;
    }

    @CheckForNull
    public Boolean getInit() {
        return init;
    }

    public HostConfig withInit(Boolean init) {
        this.init = init;
        return this;
    }

    @CheckForNull
    public Long getCpuCount() {
        return cpuCount;
    }

    public HostConfig withCpuCount(Long cpuCount) {
        this.cpuCount = cpuCount;
        return this;
    }

    @CheckForNull
    public Long getCpuPercent() {
        return cpuPercent;
    }

    public HostConfig withCpuPercent(Long cpuPercent) {
        this.cpuPercent = cpuPercent;
        return this;
    }

    @CheckForNull
    public Long getIoMaximumIOps() {
        return ioMaximumIOps;
    }

    public HostConfig withIoMaximumIOps(Long ioMaximumIOps) {
        this.ioMaximumIOps = ioMaximumIOps;
        return this;
    }

    @CheckForNull
    public Long getIoMaximumBandwidth() {
        return ioMaximumBandwidth;
    }

    public HostConfig withIoMaximumBandwidth(Long ioMaximumBandwidth) {
        this.ioMaximumBandwidth = ioMaximumBandwidth;
        return this;
    }

    @CheckForNull
    public List<Mount> getMounts() {
        return mounts;
    }

    public HostConfig withMounts(List<Mount> mounts) {
        this.mounts = mounts;
        return this;
    }

    @CheckForNull
    public List<String> getDnsOptions() {
        return dnsOptions;
    }

    public HostConfig withDnsOptions(List<String> dnsOptions) {
        this.dnsOptions = dnsOptions;
        return this;
    }

    @CheckForNull
    public List<String> getGroupAdd() {
        return groupAdd;
    }

    public HostConfig withGroupAdd(List<String> groupAdd) {
        this.groupAdd = groupAdd;
        return this;
    }

    @CheckForNull
    public String getIpcMode() {
        return ipcMode;
    }

    public HostConfig withIpcMode(String ipcMode) {
        this.ipcMode = ipcMode;
        return this;
    }

    @CheckForNull
    public String getCgroup() {
        return cgroup;
    }

    public HostConfig withCgroup(String cgroup) {
        this.cgroup = cgroup;
        return this;
    }

    @CheckForNull
    public Map<String, String> getStorageOpt() {
        return storageOpt;
    }

    public HostConfig withStorageOpt(Map<String, String> storageOpt) {
        this.storageOpt = storageOpt;
        return this;
    }

    @CheckForNull
    public String getUtSMode() {
        return utSMode;
    }

    public HostConfig withUtSMode(String utSMode) {
        this.utSMode = utSMode;
        return this;
    }

    @CheckForNull
    public String getUsernsMode() {
        return usernsMode;
    }

    public HostConfig withUsernsMode(String usernsMode) {
        this.usernsMode = usernsMode;
        return this;
    }

    @CheckForNull
    public Map<String, String> getSysctls() {
        return sysctls;
    }

    public HostConfig withSysctls(Map<String, String> sysctls) {
        this.sysctls = sysctls;
        return this;
    }

    @CheckForNull
    public List<Integer> getConsoleSize() {
        return consoleSize;
    }

    public HostConfig withConsoleSize(List<Integer> consoleSize) {
        this.consoleSize = consoleSize;
        return this;
    }

    @CheckForNull
    public Isolation getIsolation() {
        return isolation;
    }

    public HostConfig withIsolation(Isolation isolation) {
        this.isolation = isolation;
        return this;
    }

    @CheckForNull
    public Long getCpuRealtimePeriod() {
        return cpuRealtimePeriod;
    }

    public HostConfig withCpuRealtimePeriod(Long cpuRealtimePeriod) {
        this.cpuRealtimePeriod = cpuRealtimePeriod;
        return this;
    }

    @CheckForNull
    public Long getCpuRealtimeRuntime() {
        return cpuRealtimeRuntime;
    }

    public HostConfig withCpuRealtimeRuntime(Long cpuRealtimeRuntime) {
        this.cpuRealtimeRuntime = cpuRealtimeRuntime;
        return this;
    }
}
