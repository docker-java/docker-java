package com.github.dockerjava.jaxrs;

import static jersey.repackaged.com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.command.EventCallback;
import com.github.dockerjava.api.command.EventsCmd;
import com.github.dockerjava.api.model.Event;

public class EventsCmdExec extends AbstrDockerCmdExec<EventsCmd, ExecutorService> implements EventsCmd.Exec {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventsCmdExec.class);
    
    public EventsCmdExec(WebTarget baseResource) {
        super(baseResource);
    }

    @Override
    protected ExecutorService execute(EventsCmd command) {
    	ExecutorService executorService = Executors.newSingleThreadExecutor();
    	
        WebTarget webResource = getBaseResource().path("/events")
                .queryParam("since", command.getSince())
                .queryParam("until", command.getUntil());

        LOGGER.trace("GET: {}", webResource);
        EventNotifier eventNotifier = EventNotifier.create(command.getEventCallback(), webResource);
        executorService.submit(eventNotifier);
        return executorService;
    }
    
    private static class EventNotifier implements Callable<Void> {
        private static final JsonFactory JSON_FACTORY = new JsonFactory();
        private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

        private final EventCallback eventCallback;
        private final WebTarget webTarget;

        private EventNotifier(EventCallback eventCallback, WebTarget webTarget) {
            this.eventCallback = eventCallback;
            this.webTarget = webTarget;
        }

        public static EventNotifier create(EventCallback eventCallback, WebTarget webTarget) {
            checkNotNull(eventCallback, "An EventCallback must be provided");
            checkNotNull(webTarget, "An WebTarget must be provided");
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
                while (jp.nextToken() != JsonToken.END_OBJECT && !jp.isClosed() && eventCallback.isReceiving()) {
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
