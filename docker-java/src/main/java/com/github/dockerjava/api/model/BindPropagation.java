package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public enum BindPropagation {
    @JsonProperty("rprivate")
    R_PRIVATE,

    @JsonProperty("private")
    PRIVATE,

    @JsonProperty("rshared")
    R_SHARED,

    @JsonProperty("shared")
    SHARED,

    @JsonProperty("rslave")
    R_SLAVE,

    @JsonProperty("slave")
    SLAVE
}
