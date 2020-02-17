package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * BlkioStat is not documented in pubic docker swapper.yaml yet, reference:
 * https://github.com/moby/moby/blob/master/api/types/stats.go
 */
@EqualsAndHashCode
@ToString
public class BlkioStatEntry implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("major")
    Long major;
    @JsonProperty("minor")
    Long minor;
    @JsonProperty("op")
    String op;
    @JsonProperty("value")
    Long value;

    public Long getMajor() {
        return major;
    }

    public BlkioStatEntry withMajor(Long major) {
        this.major = major;
        return this;
    }

    public Long getMinor() {
        return minor;
    }

    public BlkioStatEntry withMinor(Long minor) {
        this.minor = minor;
        return this;
    }

    public String getOp() {
        return op;
    }

    public BlkioStatEntry withOp(String op) {
        this.op = op;
        return this;
    }

    public Long getValue() {
        return value;
    }

    public BlkioStatEntry withValue(Long value) {
        this.value = value;
        return this;
    }
}
