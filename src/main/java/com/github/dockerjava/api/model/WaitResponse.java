package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a wait container command response
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public class WaitResponse {

    @JsonProperty("StatusCode")
    private Integer statusCode;

    public Integer getStatusCode() {
        return statusCode;
    }
}
