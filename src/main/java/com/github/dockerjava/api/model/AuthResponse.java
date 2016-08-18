package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class AuthResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("Status")
    private String status;

    public String getStatus() {
        return status;
    }
}
