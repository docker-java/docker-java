package com.github.dockerjava.core.command;

import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;

import java.io.IOException;
import java.io.InputStream;

/**
 * Breaks the input into frame. Similar to how a buffered reader would readLies.
 * <p/>
 * See:  {@link }http://docs.docker.com/v1.6/reference/api/docker_remote_api_v1.13/#attach-to-a-container}
 */
public class FrameReader implements AutoCloseable {

    private static final int HEADER_SIZE = 8;
    private final InputStream inputStream;

    public FrameReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    private static StreamType streamType(byte streamType) {
        switch (streamType) {
            case 0:
                return StreamType.STDIN;
            case 1:
                return StreamType.STDOUT;
            case 2:
                return StreamType.STDERR;
            default:
                throw new IllegalArgumentException("invalid streamType");
        }
    }

    /**
     * @return A frame, or null if no more frames.
     */
    public Frame readFrame() throws IOException {
        byte[] header = new byte[HEADER_SIZE];
        int headerSize = inputStream.read(header);

        if (headerSize == -1) {
            return null;
        }

        if (headerSize != HEADER_SIZE) {
            throw new IOException(String.format("header must be %d bytes long, but was %d", HEADER_SIZE, headerSize));
        }

        int frameSize = (header[4] << 24) + (header[5] << 16) + (header[6] << 8) + header[7];
        int payloadSize = frameSize - header.length;

        byte[] payload = new byte[payloadSize];
        int actualPayloadSize = inputStream.read(payload);
        if (actualPayloadSize != payloadSize) {
            throw new IOException(String.format("payload must be %d bytes long, but was %d", payloadSize, actualPayloadSize));
        }

        return new Frame(streamType(header[0]), payload);
    }

    @Override
    public void close() throws Exception {
        inputStream.close();
    }
}
