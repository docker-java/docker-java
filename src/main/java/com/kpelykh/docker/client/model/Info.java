package com.kpelykh.docker.client.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
public class Info {

    @JsonProperty("Debug")
    public boolean debug;

    @JsonProperty("Containers")
    public int    containers;

    @JsonProperty("Images")
    public int    images;

    @JsonProperty("NFd")
    public int    NFd;

    @JsonProperty("NGoroutines")
    public int    NGoroutines;

    @JsonProperty("MemoryLimit")
    public boolean memoryLimit;

    @JsonProperty("LXCVersion")
    public String lxcVersion;


    @JsonProperty("NEventsListener")
    public long nEventListener;

    @JsonProperty("KernelVersion")
    public String kernelVersion;

    @JsonProperty("IPv4Forwarding")
    public String IPv4Forwarding;

    @JsonProperty("IndexServerAddress")
    public String IndexServerAddress;

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
