package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Represents a wait container command response
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WaitResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("StatusCode")
    private Integer statusCode;

    public Integer getStatusCode() {
        return statusCode;
    }
}
