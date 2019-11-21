package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;

import java.io.Serializable;

/**
 * Represents a wait container command response
 */
public class WaitResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldName("StatusCode")
    private Integer statusCode;

    public Integer getStatusCode() {
        return statusCode;
    }
}
