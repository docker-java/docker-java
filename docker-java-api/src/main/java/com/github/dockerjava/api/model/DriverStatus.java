package com.github.dockerjava.api.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author ben
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
