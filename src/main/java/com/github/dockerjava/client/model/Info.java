package com.github.dockerjava.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Info {

    @JsonProperty("Debug")
    private boolean debug;

    @JsonProperty("Containers")
    private int    containers;

    @JsonProperty("Driver")
    private String driver;

    @JsonProperty("DriverStatus")
    private List<Object> driverStatuses;


    @JsonProperty("Images")
    private int    images;

    @JsonProperty("IPv4Forwarding")
    private String IPv4Forwarding;

    @JsonProperty("IndexServerAddress")
    private String IndexServerAddress;


    @JsonProperty("InitPath")
    private String initPath;

    @JsonProperty("InitSha1")
    private String initSha1;

    @JsonProperty("KernelVersion")
    private String kernelVersion;

    @JsonProperty("LXCVersion")
    private String lxcVersion;

    @JsonProperty("MemoryLimit")
    private boolean memoryLimit;

    @JsonProperty("NEventsListener")
    private long nEventListener;

    @JsonProperty("NFd")
    private int    NFd;

    @JsonProperty("NGoroutines")
    private int    NGoroutines;
    
    @JsonProperty("SwapLimit")
    private int swapLimit;

    @JsonProperty("ExecutionDriver")
    private String executionDriver;

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public int getContainers() {
        return containers;
    }

    public void setContainers(int containers) {
        this.containers = containers;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public List<Object> getDriverStatuses() {
        return driverStatuses;
    }

    public void setDriverStatuses(List<Object> driverStatuses) {
        this.driverStatuses = driverStatuses;
    }

    public int getImages() {
        return images;
    }

    public void setImages(int images) {
        this.images = images;
    }

    public String getIPv4Forwarding() {
        return IPv4Forwarding;
    }

    public void setIPv4Forwarding(String IPv4Forwarding) {
        this.IPv4Forwarding = IPv4Forwarding;
    }

    public String getIndexServerAddress() {
        return IndexServerAddress;
    }

    public void setIndexServerAddress(String indexServerAddress) {
        IndexServerAddress = indexServerAddress;
    }

    public String getInitPath() {
        return initPath;
    }

    public void setInitPath(String initPath) {
        this.initPath = initPath;
    }

    public String getInitSha1() {
        return initSha1;
    }

    public void setInitSha1(String initSha1) {
        this.initSha1 = initSha1;
    }

    public String getKernelVersion() {
        return kernelVersion;
    }

    public void setKernelVersion(String kernelVersion) {
        this.kernelVersion = kernelVersion;
    }

    public String getLxcVersion() {
        return lxcVersion;
    }

    public void setLxcVersion(String lxcVersion) {
        this.lxcVersion = lxcVersion;
    }

    public boolean isMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(boolean memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public long getnEventListener() {
        return nEventListener;
    }

    public void setnEventListener(long nEventListener) {
        this.nEventListener = nEventListener;
    }

    public int getNFd() {
        return NFd;
    }

    public void setNFd(int NFd) {
        this.NFd = NFd;
    }

    public int getNGoroutines() {
        return NGoroutines;
    }

    public void setNGoroutines(int NGoroutines) {
        this.NGoroutines = NGoroutines;
    }

    public int getSwapLimit() {
        return swapLimit;
    }

    public void setSwapLimit(int swapLimit) {
        this.swapLimit = swapLimit;
    }
    public String getExecutionDriver() {
        return executionDriver;
    }

    public void setExecutionDriver(String executionDriver) {
        this.executionDriver=executionDriver;
    }
    
    @Override
    public String toString() {
        return "Info{" +
                "debug=" + debug +
                ", containers=" + containers +
                ", driver='" + driver + '\'' +
                ", driverStatuses=" + driverStatuses +
                ", images=" + images +
                ", IPv4Forwarding='" + IPv4Forwarding + '\'' +
                ", IndexServerAddress='" + IndexServerAddress + '\'' +
                ", initPath='" + initPath + '\'' +
                ", initSha1='" + initSha1 + '\'' +
                ", kernelVersion='" + kernelVersion + '\'' +
                ", lxcVersion='" + lxcVersion + '\'' +
                ", memoryLimit=" + memoryLimit +
                ", nEventListener=" + nEventListener +
                ", NFd=" + NFd +
                ", NGoroutines=" + NGoroutines +
                ", swapLimit=" + swapLimit +
                '}';
    }
}
