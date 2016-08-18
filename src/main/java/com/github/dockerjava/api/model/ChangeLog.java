package com.github.dockerjava.api.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
