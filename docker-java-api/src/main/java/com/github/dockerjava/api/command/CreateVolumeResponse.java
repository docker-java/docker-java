package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @author Marcus Linke
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode
@ToString
public class CreateVolumeResponse {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Driver")
    private String driver;

    @JsonProperty("Mountpoint")
    private String mountpoint;

    public String getName() {
        return name;
    }

    public String getDriver() {
        return driver;
    }

    public String getMountpoint() {
        return mountpoint;
    }
}
