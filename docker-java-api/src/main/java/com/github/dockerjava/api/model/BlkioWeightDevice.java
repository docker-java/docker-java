package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@EqualsAndHashCode
@ToString
public class BlkioWeightDevice implements Serializable {
    public static final long serialVersionUID = 1L;

    @JsonProperty("Path")
    private String path;

    @JsonProperty("Weight")
    private Integer weight;

    public String getPath() {
        return path;
    }

    public BlkioWeightDevice withPath(String path) {
        this.path = path;
        return this;
    }

    public Integer getWeight() {
        return weight;
    }

    public BlkioWeightDevice withWeight(Integer weight) {
        this.weight = weight;
        return this;
    }
}
