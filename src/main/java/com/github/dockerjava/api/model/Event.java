package com.github.dockerjava.api.model;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Representation of a Docker event.
 */
public class Event {
    private String status;

    private String id;

    private String from;

    private long time;

    public String getStatus() {
        return status;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
