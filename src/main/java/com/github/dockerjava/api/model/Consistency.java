package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;

/**
 * @since {@link RemoteApiVersion#VERSION_1_29}
 */
public enum Consistency {

    @JsonProperty("default")
    DEFAULT,

    @JsonProperty("consistent")
    CONSISTENT,

    @JsonProperty("cached")
    CACHED,

    @JsonProperty("delegated")
    DELEGATED
}
