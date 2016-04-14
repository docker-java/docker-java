package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SwarmLabel {

    @CheckForNull
    @JsonProperty("executiondriver")
    private String executionDriver;

    @CheckForNull
    @JsonProperty("kernelversion")
    private String kernelVersion;

    @CheckForNull
    @JsonProperty("operatingsystem")
    private String operatingSystem;

    @CheckForNull
    @JsonProperty("storagedriver")
    private String storageDriver;

    @CheckForNull
    public String getExecutionDriver() {
        return executionDriver;
    }

    @CheckForNull
    public String getKernelVersion() {
        return kernelVersion;
    }

    @CheckForNull
    public String getOperatingSystem() {
        return operatingSystem;
    }

    @CheckForNull
    public String getStorageDriver() {
        return storageDriver;
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
