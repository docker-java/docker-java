package com.github.dockerjava.api.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class NodePlatform {

    @JsonProperty("Architecture")
    private String architecture;

    @JsonProperty("OS")
    private String os;
}
