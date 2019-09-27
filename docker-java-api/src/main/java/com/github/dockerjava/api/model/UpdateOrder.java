package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @since {@link RemoteApiVersion#VERSION_1_36}
 */
public enum UpdateOrder {
    @JsonProperty("stop-first")
    STOP_FIRST,

    @JsonProperty("start-first")
    START_FIRST
}
