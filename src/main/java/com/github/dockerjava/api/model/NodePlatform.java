package com.github.dockerjava.api.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class NodePlatform {

    @JsonProperty("Architecture")
    private String architecture;

    @JsonProperty("OS")
    private String os;

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public void setOs(String os) {
        this.os = os;
    }
}
