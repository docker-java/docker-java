package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public enum Reachability {

    @JsonProperty("unknown")
    UNKNOWN,

    @JsonProperty("unreachable")
    UNREACHABLE,

    @JsonProperty("reachable")
    REACHABLE
}
