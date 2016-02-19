package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.CheckForNull;
import java.util.List;
import java.util.Map;

/**
 * Used for `/info`
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Info {

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
    @JsonProperty("cpuCfsQuota")
    private Boolean cpuCfsQuota;

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
    private Long nEventListener;

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
    private RegistryConfig registryConfig;

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

    /**
     * @see #architecture
     */
    @CheckForNull
    public String getArchitecture() {
        return architecture;
    }

    public Boolean isDebug() {
        return debug;
    }

    public Integer getContainers() {
        return containers;
    }

    /**
     * @see #cpuCfsPeriod
     */
    @CheckForNull
    public Boolean getCpuCfsPeriod() {
        return cpuCfsPeriod;
    }

    /**
     * @see #cpuCfsQuota
     */
    @CheckForNull
    public Boolean getCpuCfsQuota() {
        return cpuCfsQuota;
    }

    public String getDockerRootDir() {
        return dockerRootDir;
    }

    public String getDriver() {
        return driver;
    }

    public List<List<String>> getDriverStatuses() {
        return driverStatuses;
    }

    /**
     * @see #systemStatus
     */
    @CheckForNull
    public List<Object> getSystemStatus() {
        return systemStatus;
    }

    /**
     * @see #plugins
     */
    @CheckForNull
    public Map<String, List<String>> getPlugins() {
        return plugins;
    }

    public Integer getImages() {
        return images;
    }

    public String getIndexServerAddress() {
        return indexServerAddress;
    }

    public String getInitPath() {
        return initPath;
    }

    public String getInitSha1() {
        return initSha1;
    }

    public String getKernelVersion() {
        return kernelVersion;
    }

    public String[] getLabels() {
        return labels;
    }

    public String[] getSockets() {
        return sockets;
    }

    public Boolean isMemoryLimit() {
        return memoryLimit;
    }

    public Long getnEventListener() {
        return nEventListener;
    }

    public Long getMemTotal() {
        return memTotal;
    }

    public String getName() {
        return name;
    }

    public Integer getNGoroutines() {
        return nGoroutines;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public Boolean getSwapLimit() {
        return swapLimit;
    }

    public String getExecutionDriver() {
        return executionDriver;
    }

    /**
     * @see #experimentalBuild
     */
    @CheckForNull
    public Boolean getExperimentalBuild() {
        return experimentalBuild;
    }

    /**
     * @see #httpProxy
     */
    @CheckForNull
    public String getHttpProxy() {
        return httpProxy;
    }

    /**
     * @see #httpsProxy
     */
    @CheckForNull
    public String getHttpsProxy() {
        return httpsProxy;
    }

    /**
     * @see #systemTime
     */
    @CheckForNull
    public String getSystemTime() {
        return systemTime;
    }

    /**
     * @see #serverVersion
     */
    @CheckForNull
    public String getServerVersion() {
        return serverVersion;
    }

    // autogenerated getters
    // TODO remove `is*()` that doesn't match to primitives
    @CheckForNull
    public Integer getContainersPaused() {
        return containersPaused;
    }

    @CheckForNull
    public Integer getContainersRunning() {
        return containersRunning;
    }

    @CheckForNull
    public Integer getContainersStopped() {
        return containersStopped;
    }

    @CheckForNull
    public Boolean getDebug() {
        return debug;
    }

    @CheckForNull
    public String getDiscoveryBackend() {
        return discoveryBackend;
    }

    @CheckForNull
    public String getId() {
        return id;
    }

    @CheckForNull
    public Boolean getIpv4Forwarding() {
        return ipv4Forwarding;
    }

    @CheckForNull
    public Boolean getMemoryLimit() {
        return memoryLimit;
    }

    @CheckForNull
    public Integer getNCPU() {
        return ncpu;
    }

    @CheckForNull
    public Integer getNFd() {
        return nfd;
    }

    @CheckForNull
    public String getNoProxy() {
        return noProxy;
    }

    @CheckForNull
    public Boolean getOomKillDisable() {
        return oomKillDisable;
    }

    @CheckForNull
    public RegistryConfig getRegistryConfig() {
        return registryConfig;
    }
    // end of autogeneration

    /**
     * @see #oomScoreAdj
     */
    @CheckForNull
    public Integer getOomScoreAdj() {
        return oomScoreAdj;
    }

    /**
     * @see #osType
     */
    @CheckForNull
    public String getOsType() {
        return osType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Info info = (Info) o;

        return new EqualsBuilder()
                .append(architecture, info.architecture)
                .append(containers, info.containers)
                .append(containersStopped, info.containersStopped)
                .append(containersPaused, info.containersPaused)
                .append(containersRunning, info.containersRunning)
                .append(cpuCfsPeriod, info.cpuCfsPeriod)
                .append(cpuCfsQuota, info.cpuCfsQuota)
                .append(debug, info.debug)
                .append(discoveryBackend, info.discoveryBackend)
                .append(dockerRootDir, info.dockerRootDir)
                .append(driver, info.driver)
                .append(driverStatuses, info.driverStatuses)
                .append(systemStatus, info.systemStatus)
                .append(plugins, info.plugins)
                .append(executionDriver, info.executionDriver)
                .append(experimentalBuild, info.experimentalBuild)
                .append(httpProxy, info.httpProxy)
                .append(httpsProxy, info.httpsProxy)
                .append(id, info.id)
                .append(ipv4Forwarding, info.ipv4Forwarding)
                .append(images, info.images)
                .append(indexServerAddress, info.indexServerAddress)
                .append(initPath, info.initPath)
                .append(initSha1, info.initSha1)
                .append(kernelVersion, info.kernelVersion)
                .append(labels, info.labels)
                .append(memoryLimit, info.memoryLimit)
                .append(memTotal, info.memTotal)
                .append(name, info.name)
                .append(ncpu, info.ncpu)
                .append(nEventListener, info.nEventListener)
                .append(nfd, info.nfd)
                .append(nGoroutines, info.nGoroutines)
                .append(noProxy, info.noProxy)
                .append(oomKillDisable, info.oomKillDisable)
                .append(osType, info.osType)
                .append(oomScoreAdj, info.oomScoreAdj)
                .append(operatingSystem, info.operatingSystem)
                .append(registryConfig, info.registryConfig)
                .append(sockets, info.sockets)
                .append(swapLimit, info.swapLimit)
                .append(systemTime, info.systemTime)
                .append(serverVersion, info.serverVersion)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(architecture)
                .append(containers)
                .append(containersStopped)
                .append(containersPaused)
                .append(containersRunning)
                .append(cpuCfsPeriod)
                .append(cpuCfsQuota)
                .append(debug)
                .append(discoveryBackend)
                .append(dockerRootDir)
                .append(driver)
                .append(driverStatuses)
                .append(systemStatus)
                .append(plugins)
                .append(executionDriver)
                .append(experimentalBuild)
                .append(httpProxy)
                .append(httpsProxy)
                .append(id)
                .append(ipv4Forwarding)
                .append(images)
                .append(indexServerAddress)
                .append(initPath)
                .append(initSha1)
                .append(kernelVersion)
                .append(labels)
                .append(memoryLimit)
                .append(memTotal)
                .append(name)
                .append(ncpu)
                .append(nEventListener)
                .append(nfd)
                .append(nGoroutines)
                .append(noProxy)
                .append(oomKillDisable)
                .append(osType)
                .append(oomScoreAdj)
                .append(operatingSystem)
                .append(registryConfig)
                .append(sockets)
                .append(swapLimit)
                .append(systemTime)
                .append(serverVersion)
                .toHashCode();
    }

    /**
     * @since ~{@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_20}
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class RegistryConfig {
        @JsonProperty("IndexConfigs")
        private Map<String, IndexConfig> indexConfigs;

        @JsonProperty("InsecureRegistryCIDRs")
        private List<String> insecureRegistryCIDRs;

        /**
         * //FIXME unknown field
         */
        @JsonProperty("Mirrors")
        private Object mirrors;

        /**
         * @see #indexConfigs
         */
        @CheckForNull
        public Map<String, IndexConfig> getIndexConfigs() {
            return indexConfigs;
        }

        /**
         * @see #indexConfigs
         */
        public RegistryConfig withIndexConfigs(Map<String, IndexConfig> indexConfigs) {
            this.indexConfigs = indexConfigs;
            return this;
        }

        /**
         * @see #insecureRegistryCIDRs
         */
        @CheckForNull
        public List<String> getInsecureRegistryCIDRs() {
            return insecureRegistryCIDRs;
        }

        /**
         * @see #insecureRegistryCIDRs
         */
        public RegistryConfig withInsecureRegistryCIDRs(List<String> insecureRegistryCIDRs) {
            this.insecureRegistryCIDRs = insecureRegistryCIDRs;
            return this;
        }

        /**
         * @see #mirrors
         */
        @CheckForNull
        public Object getMirrors() {
            return mirrors;
        }

        /**
         * @see #mirrors
         */
        public RegistryConfig withMirrors(Object mirrors) {
            this.mirrors = mirrors;
            return this;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("indexConfigs", indexConfigs)
                    .append("insecureRegistryCIDRs", insecureRegistryCIDRs)
                    .append("mirrors", mirrors)
                    .toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            RegistryConfig that = (RegistryConfig) o;

            return new EqualsBuilder()
                    .append(indexConfigs, that.indexConfigs)
                    .append(insecureRegistryCIDRs, that.insecureRegistryCIDRs)
                    .append(mirrors, that.mirrors)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(indexConfigs)
                    .append(insecureRegistryCIDRs)
                    .append(mirrors)
                    .toHashCode();
        }

        /**
         * @since ~{@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_20}
         */
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static final class IndexConfig {
            @JsonProperty("Mirrors")
            private String mirrors;

            @JsonProperty("Name")
            private String name;

            @JsonProperty("Official")
            private Boolean official;

            @JsonProperty("Secure")
            private Boolean secure;

            /**
             * @see #mirrors
             */
            @CheckForNull
            public String getMirrors() {
                return mirrors;
            }

            /**
             * @see #mirrors
             */
            public IndexConfig withMirrors(String mirrors) {
                this.mirrors = mirrors;
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
            public IndexConfig withName(String name) {
                this.name = name;
                return this;
            }

            /**
             * @see #official
             */
            @CheckForNull
            public Boolean getOfficial() {
                return official;
            }

            /**
             * @see #official
             */
            public IndexConfig withOfficial(Boolean official) {
                this.official = official;
                return this;
            }

            /**
             * @see #secure
             */
            @CheckForNull
            public Boolean getSecure() {
                return secure;
            }

            /**
             * @see #secure
             */
            public IndexConfig withSecure(Boolean secure) {
                this.secure = secure;
                return this;
            }

            @Override
            public String toString() {
                return new ToStringBuilder(this)
                        .append("mirrors", mirrors)
                        .append("name", name)
                        .append("official", official)
                        .append("secure", secure)
                        .toString();
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;

                if (o == null || getClass() != o.getClass()) return false;

                IndexConfig that = (IndexConfig) o;

                return new EqualsBuilder()
                        .append(mirrors, that.mirrors)
                        .append(name, that.name)
                        .append(official, that.official)
                        .append(secure, that.secure)
                        .isEquals();
            }

            @Override
            public int hashCode() {
                return new HashCodeBuilder(17, 37)
                        .append(mirrors)
                        .append(name)
                        .append(official)
                        .append(secure)
                        .toHashCode();
            }
        }
    }
}
