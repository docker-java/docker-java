package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * For TaskResourcesSpec
 */
public class ResourceSpecs {

    @JsonProperty("MemoryBytes")
    private long memoryBytes;

    @JsonProperty("NanoCPUs")
    private long nanoCPUs;

    public long getMemoryBytes() {
        return memoryBytes;
    }

    public long getNanoCPUs() {
        return nanoCPUs;
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
