package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public enum ServiceRestartCondition {

    @JsonProperty("on-failure")
    ON_FAILURE,

    @JsonProperty("any")
    ANY,

    @JsonProperty("none")
    NONE

}
