package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Map;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public class VolumeOptions implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("NoCopy")
    private Boolean noCopy;

    /**
     * @since 1.24
     */
    @JsonProperty("Labels")
    private Map<String, String> labels;

    /**
     * @since 1.24
     */
    @JsonProperty("DriverConfig")
    private Driver driverConfig;

    /**
     * @see #noCopy
     */
    public Boolean getNoCopy() {
        return noCopy;
    }

    /**
     * @see #noCopy
     */
    public VolumeOptions withNoCopy(Boolean noCopy) {
        this.noCopy = noCopy;
        return this;
    }

    /**
     * @see #labels
     */
    public Map<String, String> getLabels() {
        return labels;
    }

    /**
     * @see #labels
     */
    public VolumeOptions withLabels(Map<String, String> labels) {
        this.labels = labels;
        return this;
    }

    /**
     * @see #driverConfig
     */
    public Driver getDriverConfig() {
        return driverConfig;
    }

    /**
     * @see #driverConfig
     */
    public VolumeOptions withDriverConfig(Driver driverConfig) {
        this.driverConfig = driverConfig;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
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
