package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.CheckForNull;

/**
 * part of {@link GraphDriver}
 * @author Kanstantsin Shautsou
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GraphData {

    @JsonProperty("RootDir")
    private String rootDir;

    @JsonProperty("DeviceId")
    private String deviceId;

    @JsonProperty("DeviceName")
    private String deviceName;

    @JsonProperty("DeviceSize")
    private String deviceSize;

    /**
     * @see #rootDir
     */
    @CheckForNull
    public String getRootDir() {
        return rootDir;
    }

    /**
     * @see #rootDir
     */
    public GraphData withRootDir(String rootDir) {
        this.rootDir = rootDir;
        return this;
    }

    /**
     * @see #deviceId
     */
    @CheckForNull
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * @see #deviceId
     */
    public GraphData withDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    /**
     * @see #deviceName
     */
    @CheckForNull
    public String getDeviceName() {
        return deviceName;
    }

    /**
     * @see #deviceName
     */
    public GraphData withDeviceName(String deviceName) {
        this.deviceName = deviceName;
        return this;
    }

    /**
     * @see #deviceSize
     */
    @CheckForNull
    public String getDeviceSize() {
        return deviceSize;
    }

    /**
     * @see #deviceSize
     */
    public GraphData withDeviceSize(String deviceSize) {
        this.deviceSize = deviceSize;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
