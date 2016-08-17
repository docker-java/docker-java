package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ResolutionMode {

    @JsonProperty("vip")
    VIP,

    @JsonProperty("dnsrr")
    DNSRR

}
