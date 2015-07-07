package com.github.dockerjava.jaxrs;

import static com.google.common.net.UrlEscapers.*;

import javax.ws.rs.client.WebTarget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.EventsCmd;
import com.github.dockerjava.api.model.Event;
import com.github.dockerjava.core.async.JsonStreamProcessor;
import com.github.dockerjava.jaxrs.async.AbstractCallbackNotifier;
import com.github.dockerjava.jaxrs.async.GETCallbackNotifier;

public class EventsCmdExec extends AbstrDockerCmdExec<EventsCmd, Void> implements EventsCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventsCmdExec.class);

    public EventsCmdExec(WebTarget baseResource) {
        super(baseResource);
    }

    @Override
    protected Void execute(EventsCmd command) {

        WebTarget webTarget = getBaseResource().path("/events").queryParam("since", command.getSince())
                .queryParam("until", command.getUntil());

        if (command.getFilters() != null) {
            webTarget = webTarget.queryParam("filters",
                    urlPathSegmentEscaper().escape(command.getFilters().toString()));
        }

        LOGGER.trace("GET: {}", webTarget);

        GETCallbackNotifier<Event> callbackNotifier = new GETCallbackNotifier<Event>(new JsonStreamProcessor<Event>(
                Event.class), command.getResultCallback(), webTarget);

        AbstractCallbackNotifier.startAsyncProcessing(callbackNotifier);

        return null;
    }
}
