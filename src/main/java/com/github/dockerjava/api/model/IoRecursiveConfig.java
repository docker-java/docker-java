package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IoRecursiveConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("major")
    private Long major;

    @JsonProperty("minor")
    private Long minor;

    @JsonProperty("op")
    private Long op;

    @JsonProperty("value")
    private Long value;

    /**
     * @see #major
     */
    @CheckForNull
    public Long getMajor() {
        return major;
    }

    /**
     * @see #minor
     */
    @CheckForNull
    public Long getMinor() {
        return minor;
    }

    /**
     * @see #op
     */
    public Long getOp() {
        return op;
    }

    /**
     * @see #value
     */
    public Long getValue() {
        return value;
    }
}
