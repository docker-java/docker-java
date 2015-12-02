package com.github.dockerjava.api.model;

import java.io.Serializable;

import javax.annotation.CheckForNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    private Long time;

    @JsonProperty("errorDetail")
    private ErrorDetail errorDetail;

    @Deprecated
    @JsonProperty("error")
    private String error;

    @CheckForNull
    public String getStream() {
        return stream;
    }

    @CheckForNull
    public String getStatus() {
        return status;
    }

    @CheckForNull
    public ProgressDetail getProgressDetail() {
        return progressDetail;
    }

    @CheckForNull
    @Deprecated
    public String getProgress() {
        return progress;
    }

    @CheckForNull
    public String getId() {
        return id;
    }

    @CheckForNull
    public String getFrom() {
        return from;
    }

    @CheckForNull
    public Long getTime() {
        return time;
    }

    @CheckForNull
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
        Long current;

        @JsonProperty("total")
        Long total;

        @JsonProperty("start")
        Long start;

        @CheckForNull
        public Long getCurrent() {
            return current;
        }

        @CheckForNull
        public Long getTotal() {
            return total;
        }

        @CheckForNull
        public Long getStart() {
            return start;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = false)
    public static class ErrorDetail implements Serializable {
        private static final long serialVersionUID = -9136704865403084083L;

        @JsonProperty("code")
        Integer code;

        @JsonProperty("message")
        String message;

        @CheckForNull
        public Integer getCode() {
            return code;
        }

        @CheckForNull
        public String getMessage() {
            return message;
        }

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
