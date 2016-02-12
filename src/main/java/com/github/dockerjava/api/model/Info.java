package com.github.dockerjava.api.model;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Info {

    @JsonProperty("Containers")
    private Integer containers;

    @JsonProperty("Debug")
    private Boolean debug;

    @JsonProperty("DockerRootDir")
    private String dockerRootDir;

    @JsonProperty("Driver")
    private String driver;

    @JsonProperty("DriverStatus")
    private List<Object> driverStatuses;

    @JsonProperty("ExecutionDriver")
    private String executionDriver;

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

    @JsonProperty("OperatingSystem")
    private String operatingSystem;

    @JsonProperty("Sockets")
    private String[] sockets;

    @JsonProperty("SwapLimit")
    private Boolean swapLimit;

    public Boolean isDebug() {
        return debug;
    }

    public Integer getContainers() {
        return containers;
    }

    public String getDockerRootDir() {
        return dockerRootDir;
    }

    public String getDriver() {
        return driver;
    }

    public List<Object> getDriverStatuses() {
        return driverStatuses;
    }

    public Integer getImages() {
        return images;
    }

    public String getID() {
        return id;
    }

    public Boolean getIPv4Forwarding() {
        return ipv4Forwarding;
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

    public Integer getNCPU() {
        return ncpu;
    }

    public Integer getNFd() {
        return nfd;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
