package com.github.dockerjava.api.model;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.command.EventCallback;
import com.google.common.base.Preconditions;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.concurrent.Callable;

/**
 * EventNotifier API
 */
public class EventNotifier implements Callable<Void> {
    private static final JsonFactory JSON_FACTORY = new JsonFactory();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final EventCallback eventCallback;
    private final WebTarget webTarget;

    private EventNotifier(EventCallback eventCallback, WebTarget webTarget) {
        this.eventCallback = eventCallback;
        this.webTarget = webTarget;
    }

    public static EventNotifier create(EventCallback eventCallback, WebTarget webTarget) {
        Preconditions.checkNotNull(eventCallback, "An EventCallback must be provided");
        Preconditions.checkNotNull(webTarget, "An WebTarget must be provided");
        return new EventNotifier(eventCallback, webTarget);
    }

    @Override
    public Void call() throws Exception {
        int numEvents=0;
        Response response = null;
        try {
            response = webTarget.request().get(Response.class);
            InputStream inputStream = response.readEntity(InputStream.class);
            JsonParser jp = JSON_FACTORY.createParser(inputStream);
            while (jp.nextToken() != JsonToken.END_OBJECT && !jp.isClosed()) {
                eventCallback.onEvent(OBJECT_MAPPER.readValue(jp, Event.class));
                numEvents++;
            }
        }
        catch(Exception e) {
            eventCallback.onException(e);
        }
        finally {
            if (response != null) {
                response.close();
            }
        }
        eventCallback.onCompletion(numEvents);
        return null;
    }
}
