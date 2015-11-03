package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

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
        return ToStringBuilder.reflectionToString(this);
    }
}
