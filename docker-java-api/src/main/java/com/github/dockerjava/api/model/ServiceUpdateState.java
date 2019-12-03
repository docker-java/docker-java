package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public enum ServiceUpdateState {
    @JsonProperty("updating")
    UPDATING,

    @JsonProperty("paused")
    PAUSED,

    @JsonProperty("completed")
    COMPLETED,

    @JsonProperty("rollback_completed")
    ROLLBACK_COMPLETED
}
