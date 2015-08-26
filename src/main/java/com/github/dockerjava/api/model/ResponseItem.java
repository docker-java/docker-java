package com.github.dockerjava.api.model;

import java.io.Serializable;

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

    @JsonProperty("status")
    private String status;

    @JsonProperty("progress")
    private String progress;

    @JsonProperty("progressDetail")
    private ProgressDetail progressDetail;

    @JsonProperty("error")
    private String error;

    @JsonProperty("errorDetail")
    private ErrorDetail errorDetail;

    @JsonProperty("id")
    private String id;

    public String getStatus() {
        return status;
    }

    public String getProgress() {
        return progress;
    }

    public ProgressDetail getProgressDetail() {
        return progressDetail;
    }

    public String getId() {
        return id;
    }

    public String getError() {
        return error;
    }

    public ErrorDetail getErrorDetail() {
        return errorDetail;
    }

    @JsonIgnoreProperties(ignoreUnknown = false)
    public static class ProgressDetail implements Serializable {
        private static final long serialVersionUID = -1954994695645715264L;

        @JsonProperty("current")
        Integer current;

        @JsonProperty("total")
        Integer total;

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = false)
    public static class ErrorDetail implements Serializable {
        private static final long serialVersionUID = -9136704865403084083L;

        @JsonProperty("code")
        String code;

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
