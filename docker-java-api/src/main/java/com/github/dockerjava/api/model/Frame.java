package com.github.dockerjava.api.model;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Represents a logging frame.
 */
@EqualsAndHashCode
public class Frame implements Serializable {
    private static final long serialVersionUID = 1L;

    private final StreamType streamType;

    private final byte[] payload;

    public Frame(StreamType streamType, byte[] payload) {
        this.streamType = streamType;
        this.payload = payload;
    }

    public StreamType getStreamType() {
        return streamType;
    }

    public byte[] getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", streamType, new String(payload).trim());
    }
}
