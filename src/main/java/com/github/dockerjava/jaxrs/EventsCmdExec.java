package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.EventsCmd;
import com.github.dockerjava.api.model.EventNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.InputStream;

public class EventsCmdExec extends AbstrDockerCmdExec<EventsCmd, EventNotifier> implements EventsCmd.Exec {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventsCmdExec.class);

    public EventsCmdExec(WebTarget baseResource) {
        super(baseResource);
    }

    @Override
    protected EventNotifier execute(EventsCmd command) {
        WebTarget webResource = getBaseResource().path("/events")
                .queryParam("since", command.getSince())
                .queryParam("until", command.getUntil());

        LOGGER.trace("GET: {}", webResource);
        InputStream inputStream = webResource.request().get(Response.class).readEntity(InputStream.class);
        return EventNotifier.create(command.getEventCallback(), inputStream);
    }
}
