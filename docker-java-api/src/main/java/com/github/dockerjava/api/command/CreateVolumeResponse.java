package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.model.DockerObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Map;

/**
 *
 * @author Marcus Linke
 */
@EqualsAndHashCode(callSuper = true)
@ToString
public class CreateVolumeResponse extends DockerObject {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Labels")
    private Map<String, String> labels;

    @JsonProperty("Driver")
    private String driver;

    @JsonProperty("Mountpoint")
    private String mountpoint;

    public String getName() {
        return name;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public String getDriver() {
        return driver;
    }

    public String getMountpoint() {
        return mountpoint;
    }
}
