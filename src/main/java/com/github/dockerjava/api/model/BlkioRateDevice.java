package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

import static org.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE;

public class BlkioRateDevice implements Serializable {
    public static final long serialVersionUID = 1L;

    @JsonProperty("Path")
    private String path;

    @JsonProperty("Rate")
    private Long rate;

    public String getPath() {
        return path;
    }

    public BlkioRateDevice withPath(String path) {
        this.path = path;
        return this;
    }

    public Long getRate() {
        return rate;
    }

    public BlkioRateDevice withRate(Long rate) {
        this.rate = rate;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, SHORT_PREFIX_STYLE);
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
