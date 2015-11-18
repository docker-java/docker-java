package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ErrorDetail {
    @JsonProperty
    private String message;

    public String getMessage() {
        return message;
    }
}
