package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@EqualsAndHashCode
@ToString
public class BlkioWeightDevice implements Serializable {
    public static final long serialVersionUID = 1L;

    @FieldName("Path")
    private String path;

    @FieldName("Weight")
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
