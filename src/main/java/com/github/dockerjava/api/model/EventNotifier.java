package com.github.dockerjava.api.model;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.command.EventCallback;
import com.google.common.base.Preconditions;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;

/**
 * EventStream API
 * <p/>
 * Spawns a thread to poll for events to fill a BlockingQueue
 */
public class EventNotifier implements Closeable, Callable<Void> {
    private static final JsonFactory JSON_FACTORY = new JsonFactory();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final EventCallback eventCallback;
    private final InputStream inputStream;

    private EventNotifier(EventCallback eventCallback, InputStream inputStream) {
        this.eventCallback = eventCallback;
        this.inputStream = inputStream;
    }

    public static EventNotifier create(EventCallback eventCallback, InputStream inputStream) {
        Preconditions.checkNotNull(eventCallback, "An EventCallback must be provided");
        Preconditions.checkNotNull(inputStream, "An InputStream must be provided");
        return new EventNotifier(eventCallback, inputStream);
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

    @Override
    public Void call() throws Exception {
        JsonParser jp = JSON_FACTORY.createParser(inputStream);
        while (jp.nextToken() != JsonToken.END_OBJECT && !jp.isClosed()) {
            eventCallback.onEvent(OBJECT_MAPPER.readValue(jp, Event.class));
        }
        return null;
    }
}
