package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Used for `/info`
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 */
@EqualsAndHashCode
@ToString
public class Info implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("Architecture")
    private String architecture;

    @JsonProperty("Containers")
    private Integer containers;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("ContainersStopped")
    private Integer containersStopped;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("ContainersPaused")
    private Integer containersPaused;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("ContainersRunning")
    private Integer containersRunning;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_20}
     */
    @JsonProperty("CpuCfsPeriod")
    private Boolean cpuCfsPeriod;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_20}
     */
    @JsonProperty("CpuCfsQuota")
    private Boolean cpuCfsQuota;

    @JsonProperty("CPUShares")
    private Boolean cpuShares;

    @JsonProperty("CPUSet")
    private Boolean cpuSet;

    @JsonProperty("Debug")
    private Boolean debug;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_21}
     */
    @JsonProperty("DiscoveryBackend")
    private String discoveryBackend;

    @JsonProperty("DockerRootDir")
    private String dockerRootDir;

    @JsonProperty("Driver")
    private String driver;

    @JsonProperty("DriverStatus")
    private List<List<String>> driverStatuses;

    @JsonProperty("SystemStatus")
    private List<Object> systemStatus;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("Plugins")
    private Map<String, List<String>> plugins;

    @JsonProperty("ExecutionDriver")
    private String executionDriver;

    @JsonProperty("LoggingDriver")
    private String loggingDriver;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_20}
     */
    @JsonProperty("ExperimentalBuild")
    private Boolean experimentalBuild;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_20}
     */
    @JsonProperty("HttpProxy")
    private String httpProxy;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_20}
     */
    @JsonProperty("HttpsProxy")
    private String httpsProxy;

    @JsonProperty("ID")
    private String id;

    @JsonProperty("IPv4Forwarding")
    private Boolean ipv4Forwarding;

    @JsonProperty("BridgeNfIptables")
    private Boolean bridgeNfIptables;

    @JsonProperty("BridgeNfIp6tables")
    private Boolean bridgeNfIp6tables;

    @JsonProperty("Images")
    private Integer images;

    @JsonProperty("IndexServerAddress")
    private String indexServerAddress;

    @JsonProperty("InitPath")
    private String initPath;

    @JsonProperty("InitSha1")
    private String initSha1;

    @JsonProperty("KernelVersion")
    private String kernelVersion;

    @JsonProperty("Labels")
    private String[] labels;

    @JsonProperty("MemoryLimit")
    private Boolean memoryLimit;

    @JsonProperty("MemTotal")
    private Long memTotal;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("NCPU")
    private Integer ncpu;

    @JsonProperty("NEventsListener")
    private Integer nEventsListener;

    @JsonProperty("NFd")
    private Integer nfd;

    @JsonProperty("NGoroutines")
    private Integer nGoroutines;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_20}
     */
    @JsonProperty("NoProxy")
    private String noProxy;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_20}
     */
    @JsonProperty("OomKillDisable")
    private Boolean oomKillDisable;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("OSType")
    private String osType;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("OomScoreAdj")
    private Integer oomScoreAdj;

    @JsonProperty("OperatingSystem")
    private String operatingSystem;

    @JsonProperty("RegistryConfig")
    private InfoRegistryConfig registryConfig;

    @JsonProperty("Sockets")
    private String[] sockets;

    @JsonProperty("SwapLimit")
    private Boolean swapLimit;

    /**
     * @since ~{@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_20}
     */
    @JsonProperty("SystemTime")
    private String systemTime;

    /**
     * @since ~{@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_21}
     */
    @JsonProperty("ServerVersion")
    private String serverVersion;

    @JsonProperty("ClusterStore")
    private String clusterStore;

    @JsonProperty("ClusterAdvertise")
    private String clusterAdvertise;

    /**
     * @since 1.24
     */
    @JsonProperty("Swarm")
    private SwarmInfo swarm;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_25}
     */
    @JsonProperty("Isolation")
    private String isolation;

    /**
     * @see #architecture
     */
    @CheckForNull
    public String getArchitecture() {
        return architecture;
    }

    /**
     * @see #architecture
     */
    public Info withArchitecture(String architecture) {
        this.architecture = architecture;
        return this;
    }

    /**
     * @see #containers
     */
    @CheckForNull
    public Integer getContainers() {
        return containers;
    }

    /**
     * @see #containers
     */
    public Info withContainers(Integer containers) {
        this.containers = containers;
        return this;
    }

    /**
     * @see #containersPaused
     */
    @CheckForNull
    public Integer getContainersPaused() {
        return containersPaused;
    }

    /**
     * @see #containersPaused
     */
    public Info withContainersPaused(Integer containersPaused) {
        this.containersPaused = containersPaused;
        return this;
    }

    /**
     * @see #containersRunning
     */
    @CheckForNull
    public Integer getContainersRunning() {
        return containersRunning;
    }

    /**
     * @see #containersRunning
     */
    public Info withContainersRunning(Integer containersRunning) {
        this.containersRunning = containersRunning;
        return this;
    }

    /**
     * @see #containersStopped
     */
    @CheckForNull
    public Integer getContainersStopped() {
        return containersStopped;
    }

    /**
     * @see #containersStopped
     */
    public Info withContainersStopped(Integer containersStopped) {
        this.containersStopped = containersStopped;
        return this;
    }

    /**
     * @see #cpuCfsPeriod
     */
    @CheckForNull
    public Boolean getCpuCfsPeriod() {
        return cpuCfsPeriod;
    }

    /**
     * @see #cpuCfsPeriod
     */
    public Info withCpuCfsPeriod(Boolean cpuCfsPeriod) {
        this.cpuCfsPeriod = cpuCfsPeriod;
        return this;
    }

    /**
     * @see #cpuCfsQuota
     */
    @CheckForNull
    public Boolean getCpuCfsQuota() {
        return cpuCfsQuota;
    }

    /**
     * @see #cpuCfsQuota
     */
    public Info withCpuCfsQuota(Boolean cpuCfsQuota) {
        this.cpuCfsQuota = cpuCfsQuota;
        return this;
    }

    /**
     * @see #cpuShares
     */
    @CheckForNull
    public Boolean getCpuShares() {
        return cpuShares;
    }

    /**
     * @see #cpuShares
     */
    public Info withCpuShares(Boolean cpuShares) {
        this.cpuShares = cpuShares;
        return this;
    }

    /**
     * @see #cpuSet
     */
    @CheckForNull
    public Boolean getCpuSet() {
        return cpuSet;
    }

    /**
     * @see #cpuSet
     */
    public Info withCpuSet(Boolean cpuSet) {
        this.cpuSet = cpuSet;
        return this;
    }

    /**
     * @see #debug
     */
    @CheckForNull
    public Boolean getDebug() {
        return debug;
    }

    /**
     * @see #debug
     */
    public Info withDebug(Boolean debug) {
        this.debug = debug;
        return this;
    }

    /**
     * @see #discoveryBackend
     */
    @CheckForNull
    public String getDiscoveryBackend() {
        return discoveryBackend;
    }

    /**
     * @see #discoveryBackend
     */
    public Info withDiscoveryBackend(String discoveryBackend) {
        this.discoveryBackend = discoveryBackend;
        return this;
    }

    /**
     * @see #dockerRootDir
     */
    @CheckForNull
    public String getDockerRootDir() {
        return dockerRootDir;
    }

    /**
     * @see #dockerRootDir
     */
    public Info withDockerRootDir(String dockerRootDir) {
        this.dockerRootDir = dockerRootDir;
        return this;
    }

    /**
     * @see #driver
     */
    @CheckForNull
    public String getDriver() {
        return driver;
    }

    /**
     * @see #driver
     */
    public Info withDriver(String driver) {
        this.driver = driver;
        return this;
    }

    /**
     * @see #driverStatuses
     */
    @CheckForNull
    public List<List<String>> getDriverStatuses() {
        return driverStatuses;
    }

    /**
     * @see #driverStatuses
     */
    public Info withDriverStatuses(List<List<String>> driverStatuses) {
        this.driverStatuses = driverStatuses;
        return this;
    }

    /**
     * @see #executionDriver
     */
    @CheckForNull
    public String getExecutionDriver() {
        return executionDriver;
    }

    /**
     * @see #executionDriver
     */
    public Info withExecutionDriver(String executionDriver) {
        this.executionDriver = executionDriver;
        return this;
    }

    /**
     * @see #loggingDriver
     */
    @CheckForNull
    public String getLoggingDriver() {
        return loggingDriver;
    }

    /**
     * @see #loggingDriver
     */
    public Info withLoggingDriver(String loggingDriver) {
        this.loggingDriver = loggingDriver;
        return this;
    }

    /**
     * @see #experimentalBuild
     */
    @CheckForNull
    public Boolean getExperimentalBuild() {
        return experimentalBuild;
    }

    /**
     * @see #experimentalBuild
     */
    public Info withExperimentalBuild(Boolean experimentalBuild) {
        this.experimentalBuild = experimentalBuild;
        return this;
    }

    /**
     * @see #httpProxy
     */
    @CheckForNull
    public String getHttpProxy() {
        return httpProxy;
    }

    /**
     * @see #httpProxy
     */
    public Info withHttpProxy(String httpProxy) {
        this.httpProxy = httpProxy;
        return this;
    }

    /**
     * @see #httpsProxy
     */
    @CheckForNull
    public String getHttpsProxy() {
        return httpsProxy;
    }

    /**
     * @see #httpsProxy
     */
    public Info withHttpsProxy(String httpsProxy) {
        this.httpsProxy = httpsProxy;
        return this;
    }

    /**
     * @see #id
     */
    @CheckForNull
    public String getId() {
        return id;
    }

    /**
     * @see #id
     */
    public Info withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * @see #images
     */
    @CheckForNull
    public Integer getImages() {
        return images;
    }

    /**
     * @see #images
     */
    public Info withImages(Integer images) {
        this.images = images;
        return this;
    }

    /**
     * @see #indexServerAddress
     */
    @CheckForNull
    public String getIndexServerAddress() {
        return indexServerAddress;
    }

    /**
     * @see #indexServerAddress
     */
    public Info withIndexServerAddress(String indexServerAddress) {
        this.indexServerAddress = indexServerAddress;
        return this;
    }

    /**
     * @see #initPath
     */
    @CheckForNull
    public String getInitPath() {
        return initPath;
    }

    /**
     * @see #initPath
     */
    public Info withInitPath(String initPath) {
        this.initPath = initPath;
        return this;
    }

    /**
     * @see #initSha1
     */
    @CheckForNull
    public String getInitSha1() {
        return initSha1;
    }

    /**
     * @see #initSha1
     */
    public Info withInitSha1(String initSha1) {
        this.initSha1 = initSha1;
        return this;
    }

    /**
     * @see #ipv4Forwarding
     */
    @CheckForNull
    public Boolean getIPv4Forwarding() {
        return ipv4Forwarding;
    }

    /**
     * @see #ipv4Forwarding
     */
    public Info withIPv4Forwarding(Boolean ipv4Forwarding) {
        this.ipv4Forwarding = ipv4Forwarding;
        return this;
    }

    /**
     * @see #bridgeNfIptables
     */
    @CheckForNull
    public Boolean getBridgeNfIptables() {
        return bridgeNfIptables;
    }

    /**
     * @see #bridgeNfIptables
     */
    public Info withBridgeNfIptables(Boolean bridgeNfIptables) {
        this.bridgeNfIptables = bridgeNfIptables;
        return this;
    }

    /**
     * @see #bridgeNfIp6tables
     */
    @CheckForNull
    public Boolean getBridgeNfIp6tables() {
        return bridgeNfIp6tables;
    }

    /**
     * @see #bridgeNfIp6tables
     */
    public Info withBridgeNfIp6tables(Boolean bridgeNfIp6tables) {
        this.bridgeNfIp6tables = bridgeNfIp6tables;
        return this;
    }

    /**
     * @see #kernelVersion
     */
    @CheckForNull
    public String getKernelVersion() {
        return kernelVersion;
    }

    /**
     * @see #kernelVersion
     */
    public Info withKernelVersion(String kernelVersion) {
        this.kernelVersion = kernelVersion;
        return this;
    }

    /**
     * @see #labels
     */
    @CheckForNull
    public String[] getLabels() {
        return labels;
    }

    /**
     * @see #labels
     */
    public Info withLabels(String[] labels) {
        this.labels = labels;
        return this;
    }

    /**
     * @see #memoryLimit
     */
    @CheckForNull
    public Boolean getMemoryLimit() {
        return memoryLimit;
    }

    /**
     * @see #memoryLimit
     */
    public Info withMemoryLimit(Boolean memoryLimit) {
        this.memoryLimit = memoryLimit;
        return this;
    }

    /**
     * @see #memTotal
     */
    @CheckForNull
    public Long getMemTotal() {
        return memTotal;
    }

    /**
     * @see #memTotal
     */
    public Info withMemTotal(Long memTotal) {
        this.memTotal = memTotal;
        return this;
    }

    /**
     * @see #name
     */
    @CheckForNull
    public String getName() {
        return name;
    }

    /**
     * @see #name
     */
    public Info withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @see #ncpu
     */
    @CheckForNull
    public Integer getNCPU() {
        return ncpu;
    }

    /**
     * @see #ncpu
     */
    public Info withNCPU(Integer ncpu) {
        this.ncpu = ncpu;
        return this;
    }

    /**
     * @see #nEventsListener
     */
    @CheckForNull
    public Integer getNEventsListener() {
        return nEventsListener;
    }

    /**
     * @see #nEventsListener
     */
    public Info withNEventsListener(Integer nEventListener) {
        this.nEventsListener = nEventListener;
        return this;
    }

    /**
     * @see #nfd
     */
    @CheckForNull
    public Integer getNFd() {
        return nfd;
    }

    /**
     * @see #nfd
     */
    public Info withNFd(Integer nfd) {
        this.nfd = nfd;
        return this;
    }

    /**
     * @see #nGoroutines
     */
    @CheckForNull
    public Integer getNGoroutines() {
        return nGoroutines;
    }

    /**
     * @see #nGoroutines
     */
    public Info withNGoroutines(Integer nGoroutines) {
        this.nGoroutines = nGoroutines;
        return this;
    }

    /**
     * @see #noProxy
     */
    @CheckForNull
    public String getNoProxy() {
        return noProxy;
    }

    /**
     * @see #noProxy
     */
    public Info withNoProxy(String noProxy) {
        this.noProxy = noProxy;
        return this;
    }

    /**
     * @see #oomKillDisable
     */
    @CheckForNull
    public Boolean getOomKillDisable() {
        return oomKillDisable;
    }

    /**
     * @see #oomKillDisable
     */
    public Info withOomKillDisable(Boolean oomKillDisable) {
        this.oomKillDisable = oomKillDisable;
        return this;
    }

    /**
     * @see #oomScoreAdj
     */
    @CheckForNull
    public Integer getOomScoreAdj() {
        return oomScoreAdj;
    }

    /**
     * @see #oomScoreAdj
     */
    public Info withOomScoreAdj(Integer oomScoreAdj) {
        this.oomScoreAdj = oomScoreAdj;
        return this;
    }

    /**
     * @see #operatingSystem
     */
    @CheckForNull
    public String getOperatingSystem() {
        return operatingSystem;
    }

    /**
     * @see #operatingSystem
     */
    public Info withOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
        return this;
    }

    /**
     * @see #osType
     */
    @CheckForNull
    public String getOsType() {
        return osType;
    }

    /**
     * @see #osType
     */
    public Info withOsType(String osType) {
        this.osType = osType;
        return this;
    }

    /**
     * @see #plugins
     */
    @CheckForNull
    public Map<String, List<String>> getPlugins() {
        return plugins;
    }

    /**
     * @see #plugins
     */
    public Info withPlugins(Map<String, List<String>> plugins) {
        this.plugins = plugins;
        return this;
    }

    /**
     * @see #registryConfig
     */
    @CheckForNull
    public InfoRegistryConfig getRegistryConfig() {
        return registryConfig;
    }

    /**
     * @see #registryConfig
     */
    public Info withRegistryConfig(InfoRegistryConfig registryConfig) {
        this.registryConfig = registryConfig;
        return this;
    }

    /**
     * @see #serverVersion
     */
    @CheckForNull
    public String getServerVersion() {
        return serverVersion;
    }

    /**
     * @see #serverVersion
     */
    public Info withServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
        return this;
    }

    /**
     * @see #clusterStore
     */
    @CheckForNull
    public String getClusterStore() {
        return clusterStore;
    }

    /**
     * @see #clusterStore
     */
    public Info withClusterStore(String clusterStore) {
        this.clusterStore = clusterStore;
        return this;
    }

    /**
     * @see #clusterAdvertise
     */
    @CheckForNull
    public String getClusterAdvertise() {
        return clusterAdvertise;
    }

    /**
     * @see #clusterAdvertise
     */
    public Info withClusterAdvertise(String clusterAdvertise) {
        this.clusterAdvertise = clusterAdvertise;
        return this;
    }

    /**
     * @see #sockets
     */
    @CheckForNull
    public String[] getSockets() {
        return sockets;
    }

    /**
     * @see #sockets
     */
    public Info withSockets(String[] sockets) {
        this.sockets = sockets;
        return this;
    }

    /**
     * @see #swapLimit
     */
    @CheckForNull
    public Boolean getSwapLimit() {
        return swapLimit;
    }

    /**
     * @see #swapLimit
     */
    public Info withSwapLimit(Boolean swapLimit) {
        this.swapLimit = swapLimit;
        return this;
    }

    /**
     * @see #systemStatus
     */
    @CheckForNull
    public List<Object> getSystemStatus() {
        return systemStatus;
    }

    /**
     * @see #systemStatus
     */
    public Info withSystemStatus(List<Object> systemStatus) {
        this.systemStatus = systemStatus;
        return this;
    }

    /**
     * @see #systemTime
     */
    @CheckForNull
    public String getSystemTime() {
        return systemTime;
    }

    /**
     * @see #systemTime
     */
    public Info withSystemTime(String systemTime) {
        this.systemTime = systemTime;
        return this;
    }

    /**
     * @see #swarm
     */
    @CheckForNull
    public SwarmInfo getSwarm() {
        return swarm;
    }

    /**
     * @see #swarm
     */
    public Info withSwarm(SwarmInfo swarm) {
        this.swarm = swarm;
        return this;
    }

    /**
     * @see #isolation
     */
    @CheckForNull
    public String getIsolation() {
        return isolation;
    }

    /**
     * @see #isolation
     */
    public Info withIsolation(String isolation) {
        this.isolation = isolation;
        return this;
    }
}
