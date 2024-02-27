package com.github.dockerjava.api.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@EqualsAndHashCode
@ToString
public class RuntimeInfo extends DockerObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
