package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.EventsCmd;
import com.github.dockerjava.api.model.Event;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.async.JsonStreamProcessor;
import com.github.dockerjava.jaxrs.async.AbstractCallbackNotifier;
import com.github.dockerjava.jaxrs.async.GETCallbackNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;

import static com.google.common.net.UrlEscapers.urlPathSegmentEscaper;

public class EventsCmdExec extends AbstrAsyncDockerCmdExec<EventsCmd, Event> implements EventsCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventsCmdExec.class);

    public EventsCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected AbstractCallbackNotifier<Event> callbackNotifier(EventsCmd command, ResultCallback<Event> resultCallback) {
        WebTarget webTarget = getBaseResource().path("/events").queryParam("since", command.getSince())
                .queryParam("until", command.getUntil());

        if (command.getFilters() != null) {
            webTarget = webTarget
                    .queryParam("filters", urlPathSegmentEscaper().escape(command.getFilters().toString()));
        }

        LOGGER.trace("GET: {}", webTarget);

        return new GETCallbackNotifier<Event>(new JsonStreamProcessor<Event>(Event.class), resultCallback,
                webTarget.request());
    }
}
