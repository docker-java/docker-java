package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * For ServiceRestartPolicy
 */
public enum ServiceRestartCondition {

    @JsonProperty("on_failure")
    ON_FAILURE,

    @JsonProperty("any")
    ANY,

    @JsonProperty("none")
    NONE

}
