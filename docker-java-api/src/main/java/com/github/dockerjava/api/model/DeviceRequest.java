package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode
@ToString
public class DeviceRequest implements Serializable {
    public static final long serialVersionUID = 1L;

    @JsonProperty("Driver")
    private String driver;

    @JsonProperty("Count")
    private Integer count;

    @JsonProperty("DeviceIDs")
    private List<String> deviceIds;

    @JsonProperty("Capabilities")
    private List<List<String>> capabilities;

    @JsonProperty("Options")
    private Map<String, String> options;

    public String getDriver() {
        return driver;
    }

    public DeviceRequest withDriver(String driver) {
        this.driver = driver;
        return this;
    }

    public Integer getCount() {
        return count;
    }

    public DeviceRequest withCount(Integer count) {
        this.count = count;
        return this;
    }

    public List<String> getDeviceIds() {
        return deviceIds;
    }

    public DeviceRequest withDeviceIds(List<String> deviceIds) {
        this.deviceIds = deviceIds;
        return this;
    }

    public List<List<String>> getCapabilities() {
        return capabilities;
    }

    public DeviceRequest withCapabilities(List<List<String>> capabilities) {
        this.capabilities = capabilities;
        return this;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public DeviceRequest withOptions(Map<String, String> options) {
        this.options = options;
        return this;
    }
}
