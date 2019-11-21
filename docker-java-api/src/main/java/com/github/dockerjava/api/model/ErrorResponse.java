package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;

import java.io.Serializable;

@Deprecated
public class ErrorResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldName("errorDetail")
    private ErrorDetail errorDetail;

    @FieldName("error")
    private String error;

    public ErrorDetail getErrorDetail() {
        return errorDetail;
    }

    public String getError() {
        return error;
    }
}
