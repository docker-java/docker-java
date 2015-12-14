package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class ErrorResponse {
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
