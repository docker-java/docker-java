package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorDetail {
    @JsonProperty
    private String message;

    public String getMessage() {
        return message;
    }
}
