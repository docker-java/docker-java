package com.github.dockerjava.api.model;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import jersey.repackaged.com.google.common.base.Objects;

/**
 * Represents an event stream
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class EventStreamItem implements Serializable {

    @JsonProperty("stream")
    private String stream;

    // {"error":"Error...", "errorDetail":{"code": 123, "message": "Error..."}}
    @JsonProperty("error")
    private String error;

    @JsonProperty("errorDetail")
    private ErrorDetail errorDetail;

    public String getStream() {
        return stream;
    }

    public String getError() {
        return error;
    }

    public ErrorDetail getErrorDetail() {
        return errorDetail;
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class ErrorDetail implements Serializable {
        @JsonProperty("code")
        String code;
        @JsonProperty("message")
        String message;

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("code", code)
                    .add("message", message)
                    .toString();
        }
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("stream", stream)
                .add("error", error)
                .add("errorDetail", errorDetail)
                .toString();
    }
}
