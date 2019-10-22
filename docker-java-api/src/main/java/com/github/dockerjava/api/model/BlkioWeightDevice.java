package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "BlkioWeightDevice{" +
                "path='" + path + '\'' +
                ", weight=" + weight +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlkioWeightDevice that = (BlkioWeightDevice) o;
        return Objects.equals(path, that.path) &&
                Objects.equals(weight, that.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, weight);
    }
}
