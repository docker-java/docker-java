package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(Include.NON_NULL)
public class ErrorDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty
    private String message;

    public String getMessage() {
        return message;
    }
}
