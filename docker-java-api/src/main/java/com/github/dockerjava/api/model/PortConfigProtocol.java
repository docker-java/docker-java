package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public enum PortConfigProtocol {

    @JsonProperty("tcp")
    TCP,

    @JsonProperty("udp")
    UDP,

    @JsonProperty("sctp")
    SCTP

}
