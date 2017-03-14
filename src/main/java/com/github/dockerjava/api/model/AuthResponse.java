package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("Status")
    private String status;

    public String getStatus() {
        return status;
    }
}
