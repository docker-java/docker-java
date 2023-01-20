package com.github.dockerjava.api.model;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Represents a logging frame.
 */
@EqualsAndHashCode
public class Frame extends DockerObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private final StreamType streamType;

    private final byte[] payload;

    private final boolean isComplete;

    public Frame(StreamType streamType, byte[] payload) {
        this.streamType = streamType;
        this.payload = payload;
        this.isComplete = true;
    }

    public Frame(StreamType streamType, byte[] payload, boolean isComplete) {
        this.streamType = streamType;
        this.payload = payload;
        this.isComplete = isComplete;
    }

    public StreamType getStreamType() {
        return streamType;
    }

    public byte[] getPayload() {
        return payload;
    }

    public  boolean getIsComplete() {
        return isComplete;
    }

    @Override
    public String toString() {
        return String.format("%s: %s; isComplete: %s", streamType, new String(payload).trim(), isComplete);
    }
}
