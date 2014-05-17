package com.kpelykh.docker.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ben on 12/12/13.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverStatus {

    @JsonProperty("Root Dir")
    private String rootDir;

    @JsonProperty("Dirs")
    private int dirs;

    public String getRootDir() {
        return rootDir;
    }

    public int getDirs() {
        return dirs;
    }

    @Override
    public String toString() {
        return "DriverStatus{" +
                "rootDir='" + rootDir + '\'' +
                ", dirs=" + dirs +
                '}';
    }
}
