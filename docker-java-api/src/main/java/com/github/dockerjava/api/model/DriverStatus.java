package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ben
 */
@EqualsAndHashCode
@ToString
public class DriverStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("Root Dir")
    private String rootDir;

    @JsonProperty("Dirs")
    private Integer dirs;

    public String getRootDir() {
        return rootDir;
    }

    public Integer getDirs() {
        return dirs;
    }
}
