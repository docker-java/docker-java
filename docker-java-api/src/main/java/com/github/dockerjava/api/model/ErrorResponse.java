package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Deprecated
public class ErrorResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty
    private ErrorDetail errorDetail;

    @JsonProperty
    private String error;

    public ErrorDetail getErrorDetail() {
        return errorDetail;
    }

    public String getError() {
        return error;
    }
}
