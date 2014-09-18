package com.github.dockerjava.api.model;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Queues;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * EventStream API
 * <p/>
 * Spawns a thread to poll for events to fill a BlockingQueue
 */
public class EventStream implements Closeable {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final BlockingQueue<Event> queue;
    private final EventRunner eventRunner;

    private EventStream(InputStream inputStream) {
        queue = Queues.newLinkedBlockingQueue();
        eventRunner = new EventRunner(queue, inputStream);
    }

    public static EventStream create(InputStream inputStream) {
        return new EventStream(inputStream).startRunner();
    }

    public Event pollEvent() {
        return queue.poll();
    }

    public Event pollEvent(long timeout, TimeUnit unit) throws InterruptedException {
        return queue.poll(timeout, unit);
    }

    @Override
    public void close() throws IOException {
        eventRunner.initiateStop();
        executor.shutdown();
    }

    private EventStream startRunner() {
        executor.execute(eventRunner);
        return this;
    }

    private static class EventRunner implements Runnable {
        private static final JsonFactory JSON_FACTORY = new JsonFactory();
        private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

        private final Queue<Event> queue;
        private final InputStream inputStream;

        public EventRunner(Queue<Event> queue, InputStream inputStream) {
            this.queue = queue;
            this.inputStream = inputStream;
        }

        public void initiateStop() throws IOException {
            inputStream.close();
        }

        @Override
        public void run() {
            try {
                JsonParser jp = JSON_FACTORY.createParser(inputStream);
                while (jp.nextToken() != JsonToken.END_OBJECT && !jp.isClosed()) {
                    queue.add(OBJECT_MAPPER.readValue(jp, Event.class));
                }
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
