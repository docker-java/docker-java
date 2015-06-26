package com.github.dockerjava.core.command;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class EventStreamReader<I> implements AutoCloseable {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Class<I> type;

    private final InputStream inputStream;

    public EventStreamReader(InputStream inputStream, Class<I> type) {
        this.inputStream = inputStream;
        this.type = type;
    }

    public I readItem() throws IOException {
        try {
            return objectMapper.readValue(inputStream, type);
        } catch (IOException e) {
            // dirty, but works
            if (e.getMessage().equals("Stream closed")) {
                return null;
            }
            throw e;
        }
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }
}
