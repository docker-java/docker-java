package com.github.dockerjava.jaxrs;

import javax.ws.rs.client.WebTarget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.EventsCmd;
import com.github.dockerjava.api.model.Event;
import com.github.dockerjava.core.async.JsonStreamProcessor;
import com.github.dockerjava.jaxrs.async.AbstractCallbackNotifier;
import com.github.dockerjava.jaxrs.async.GETCallbackNotifier;

public class EventsCmdExec extends AbstrAsyncDockerCmdExec<EventsCmd, Event> implements EventsCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventsCmdExec.class);

    public EventsCmdExec(WebTarget baseResource) {
        super(baseResource);
    }

    @Override
    protected AbstractCallbackNotifier<Event> callbackNotifier(EventsCmd command, ResultCallback<Event> resultCallback) {
        WebTarget webTarget = getBaseResource().path("/events").queryParam("since", command.getSince())
                .queryParam("until", command.getUntil());

        LOGGER.trace("GET: {}", webTarget);

        return new GETCallbackNotifier<Event>(new JsonStreamProcessor<Event>(
                Event.class), resultCallback, webTarget.request());
    }
}
