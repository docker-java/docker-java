package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;
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

    @FieldName("Root Dir")
    private String rootDir;

    @FieldName("Dirs")
    private Integer dirs;

    public String getRootDir() {
        return rootDir;
    }

    public Integer getDirs() {
        return dirs;
    }
}
