package com.kpelykh.docker.client.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
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

    public boolean isDebug() {
        return debug;
    }

    public int getContainers() {
        return containers;
    }

    public String getDriver() {
        return driver;
    }

    public List<Object> getDriverStatuses() {
        return driverStatuses;
    }

    public int getImages() {
        return images;
    }

    public String getIPv4Forwarding() {
        return IPv4Forwarding;
    }

    public String getIndexServerAddress() {
        return IndexServerAddress;
    }

    public String getKernelVersion() {
        return kernelVersion;
    }

    public String getLxcVersion() {
        return lxcVersion;
    }

    public boolean isMemoryLimit() {
        return memoryLimit;
    }

    public long getnEventListener() {
        return nEventListener;
    }

    public int getNFd() {
        return NFd;
    }

    public int getNGoroutines() {
        return NGoroutines;
    }

    @Override
    public String toString() {
        return "Info{" +
                "debug=" + debug +
                ", containers=" + containers +
                ", images=" + images +
                ", NFd=" + NFd +
                ", NGoroutines=" + NGoroutines +
                ", memoryLimit=" + memoryLimit +
                ", lxcVersion='" + lxcVersion + '\'' +
                ", nEventListener=" + nEventListener +
                ", kernelVersion='" + kernelVersion + '\'' +
                ", IPv4Forwarding='" + IPv4Forwarding + '\'' +
                ", IndexServerAddress='" + IndexServerAddress + '\'' +
                '}';
    }
}
