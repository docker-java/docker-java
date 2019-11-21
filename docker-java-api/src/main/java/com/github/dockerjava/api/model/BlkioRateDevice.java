package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@EqualsAndHashCode
@ToString
public class BlkioRateDevice implements Serializable {
    public static final long serialVersionUID = 1L;

    @FieldName("Path")
    private String path;

    @FieldName("Rate")
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
