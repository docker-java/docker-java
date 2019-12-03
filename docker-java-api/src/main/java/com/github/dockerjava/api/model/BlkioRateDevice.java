package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@EqualsAndHashCode
@ToString
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
}
