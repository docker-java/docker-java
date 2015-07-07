package com.github.dockerjava.core.command;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;

/**
 * Breaks the input into frame. Similar to how a buffered reader would readLies.
 * <p/>
 * See: {@link }http://docs.docker.com/v1.6/reference/api/docker_remote_api_v1.13/#attach-to-a-container}
 */
public class FrameReader implements AutoCloseable {

    private static final int BUFFER_SIZE = 100;

    private static final int HEADER_SIZE = 8;

    private final InputStream inputStream;

    private boolean rawDetected = false;

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

        byte[] buffer = new byte[BUFFER_SIZE];

        int readBytes = inputStream.read(buffer);

        if (readBytes == -1) {
            return null;
        }

        if (rawDetected || readBytes != HEADER_SIZE) {
            rawDetected = true;

            byte[] read = Arrays.copyOfRange(buffer, 0, readBytes);

            return new Frame(StreamType.RAW, read);
        } else {

            int payloadSize = ((buffer[4] & 0xff) << 24) + ((buffer[5] & 0xff) << 16) + ((buffer[6] & 0xff) << 8)
                    + (buffer[7] & 0xff);

            byte[] payload = new byte[payloadSize];
            int actualPayloadSize = inputStream.read(payload);
            if (actualPayloadSize != payloadSize) {
                throw new IOException(String.format("payload must be %d bytes long, but was %d", payloadSize,
                        actualPayloadSize));
            }

            return new Frame(streamType(buffer[0]), payload);

        }
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

}
