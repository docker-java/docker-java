package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Represents an item returned from pull
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PullEventStreamItem implements Serializable {

    private static final long serialVersionUID = -5187169652557467828L;

    @JsonProperty("status")
    private String status;

    @JsonProperty("progress")
    private String progress;

    @JsonProperty("progressDetail")
    private ProgressDetail progressDetail;

    public String getStatus() {
        return status;
    }

    public String getProgress() {
        return progress;
    }

    public ProgressDetail getProgressDetail() {
        return progressDetail;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProgressDetail implements Serializable {
        @JsonProperty("current")
        int current;

        @Override
        public String toString() {
            return "current " + current;
        }
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("status", status).add("progress", progress)
                .add("progressDetail", progressDetail).toString();
    }
}
