package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by jan on 8/17/16.
 */
public enum PortConfigProtocol {

    @JsonProperty("tcp")
    TCP,

    @JsonProperty("udp")
    udp

}
