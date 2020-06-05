package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@EqualsAndHashCode
@ToString
public class ChangeLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("Path")
    private String path;

    @JsonProperty("Kind")
    private Integer kind;

    public String getPath() {
        return path;
    }

    public Integer getKind() {
        return kind;
    }
}
