package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
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
