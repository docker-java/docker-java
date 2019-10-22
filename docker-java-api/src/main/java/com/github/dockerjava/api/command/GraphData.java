package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.util.Objects;

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

    @JsonProperty("dir")
    private String dir;

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

    /**
     * @see #dir
     */
    @CheckForNull
    public String getDir() {
        return dir;
    }

    /**
     * @see #dir
     */
    public GraphData withDir(String dir) {
        this.dir = dir;
        return this;
    }

    @Override
    public String toString() {
        return "GraphData{" +
                "rootDir='" + rootDir + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", deviceSize='" + deviceSize + '\'' +
                ", dir='" + dir + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphData graphData = (GraphData) o;
        return Objects.equals(rootDir, graphData.rootDir) &&
                Objects.equals(deviceId, graphData.deviceId) &&
                Objects.equals(deviceName, graphData.deviceName) &&
                Objects.equals(deviceSize, graphData.deviceSize) &&
                Objects.equals(dir, graphData.dir);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rootDir, deviceId, deviceName, deviceSize, dir);
    }
}
