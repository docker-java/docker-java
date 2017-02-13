package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @since 1.24
 */
public enum LocalNodeState {

    @JsonProperty("inactive")
    INACTIVE,

    @JsonProperty("pending")
    PENDING,

    @JsonProperty("active")
    ACTIVE,

    @JsonProperty("error")
    ERROR

}
