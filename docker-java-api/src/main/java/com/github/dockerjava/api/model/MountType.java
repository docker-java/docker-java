package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public enum MountType {
    @JsonProperty("bind")
    BIND,

    @JsonProperty("volume")
    VOLUME,

    //@since 1.29
    @JsonProperty("tmpfs")
    TMPFS,

    //@since 1.40
    @JsonProperty("npipe")
    NPIPE

}
