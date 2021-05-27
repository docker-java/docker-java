package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * Represents a wait container command response
 */
@EqualsAndHashCode(callSuper = true)
@ToString
public class WaitResponse extends DockerObject implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("StatusCode")
    private Integer statusCode;

    public Integer getStatusCode() {
        return statusCode;
    }
}
