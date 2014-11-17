package com.github.dockerjava.jaxrs1;

import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.command.EventCallback;
import com.github.dockerjava.api.command.EventsCmd;
import com.github.dockerjava.api.model.Event;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class EventsCmdExec extends AbstrDockerCmdExec<EventsCmd, ExecutorService> implements EventsCmd.Exec {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventsCmdExec.class);
    
    public EventsCmdExec(WebResource baseResource) {
        super(baseResource);
    }

    @Override
    protected ExecutorService execute(EventsCmd command) {
    	ExecutorService executorService = Executors.newSingleThreadExecutor();
    	
        WebResource webResource = getBaseResource().path("/events")
                .queryParam("since", command.getSince())
                .queryParam("until", command.getUntil())
                .build();

        LOGGER.trace("GET: {}", webResource);
        EventNotifier eventNotifier = EventNotifier.create(command.getEventCallback(), webResource);
        executorService.submit(eventNotifier);
        return executorService;
    }
    
    private static class EventNotifier implements Callable<Void> {
        private static final JsonFactory JSON_FACTORY = new JsonFactory();
        private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

        private final EventCallback eventCallback;
        private final WebResource WebResource;

        private EventNotifier(EventCallback eventCallback, WebResource WebResource) {
            this.eventCallback = eventCallback;
            this.WebResource = WebResource;
        }

        public static EventNotifier create(EventCallback eventCallback, WebResource WebResource) {
            Preconditions.checkNotNull(eventCallback, "An EventCallback must be provided");
            Preconditions.checkNotNull(WebResource, "An WebResource must be provided");
            return new EventNotifier(eventCallback, WebResource);
        }

        @Override
        public Void call() throws Exception {
            int numEvents=0;
            ClientResponse response = null;
            try {
                response = WebResource.get(ClientResponse.class);
                InputStream inputStream = response.getEntityInputStream();
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
}
