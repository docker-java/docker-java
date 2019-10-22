package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

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
        return "BlkioRateDevice{" +
                "path='" + path + '\'' +
                ", rate=" + rate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlkioRateDevice that = (BlkioRateDevice) o;
        return Objects.equals(path, that.path) &&
                Objects.equals(rate, that.rate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, rate);
    }
}
