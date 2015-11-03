package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Represents a pull response stream item
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public class ResponseItem implements Serializable {

    private static final long serialVersionUID = -5187169652557467828L;

    @JsonProperty("stream")
    private String stream;

    @JsonProperty("status")
    private String status;

    @JsonProperty("progressDetail")
    private ProgressDetail progressDetail;

    @Deprecated
    @JsonProperty("progress")
    private String progress;

    @JsonProperty("id")
    private String id;

    @JsonProperty("from")
    private String from;

    @JsonProperty("time")
    private long time;

    @JsonProperty("errorDetail")
    private ErrorDetail errorDetail;

    @Deprecated
    @JsonProperty("error")
    private String error;

    public String getStream() {
        return stream;
    }

    public String getStatus() {
        return status;
    }

    public ProgressDetail getProgressDetail() {
        return progressDetail;
    }

    @Deprecated
    public String getProgress() {
        return progress;
    }

    public String getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public long getTime() {
        return time;
    }

    public ErrorDetail getErrorDetail() {
        return errorDetail;
    }

    @Deprecated
    public String getError() {
        return error;
    }

    /**
     * Returns whether the error field indicates an error
     *
     * @returns true: the error field indicates an error, false: the error field doesn't indicate an error
     */
    @JsonIgnore
    public boolean isErrorIndicated() {
        // check both the deprecated and current error fields, just in case
        return getError() != null || getErrorDetail() != null;
    }

    @JsonIgnoreProperties(ignoreUnknown = false)
    public static class ProgressDetail implements Serializable {
        private static final long serialVersionUID = -1954994695645715264L;

        @JsonProperty("current")
        long current;

        @JsonProperty("total")
        long total;

        @JsonProperty("start")
        long start;

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = false)
    public static class ErrorDetail implements Serializable {
        private static final long serialVersionUID = -9136704865403084083L;

        @JsonProperty("code")
        int code;

        @JsonProperty("message")
        String message;

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
}
