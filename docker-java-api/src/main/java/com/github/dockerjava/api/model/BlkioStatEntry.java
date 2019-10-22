package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * BlkioStat is not documented in pubic docker swapper.yaml yet, reference:
 * https://github.com/moby/moby/blob/master/api/types/stats.go
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    @Override
    public String toString() {
        return "BlkioStatEntry{" +
                "major=" + major +
                ", minor=" + minor +
                ", op='" + op + '\'' +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlkioStatEntry that = (BlkioStatEntry) o;
        return Objects.equals(major, that.major) &&
                Objects.equals(minor, that.minor) &&
                Objects.equals(op, that.op) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, op, value);
    }
}
