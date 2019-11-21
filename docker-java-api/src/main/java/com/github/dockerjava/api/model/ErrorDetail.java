package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;

import java.io.Serializable;

public class ErrorDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldName("message")
    private String message;

    public String getMessage() {
        return message;
    }
}
