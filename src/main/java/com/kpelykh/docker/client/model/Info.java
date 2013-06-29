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
    public long    containers;

    @JsonProperty("Images")
    public long    images;

    public long    NFd;

    public long    NGoroutines;

    @JsonProperty("MemoryLimit")
    public boolean memoryLimit;


    @Override
    public String toString() {
        return "Info{" +
                "debug=" + debug +
                ", containers=" + containers +
                ", images=" + images +
                ", NFd=" + NFd +
                ", NGoroutines=" + NGoroutines +
                ", memoryLimit=" + memoryLimit +
                '}';
    }

}
