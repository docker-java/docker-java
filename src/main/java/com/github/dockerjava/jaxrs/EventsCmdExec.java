package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.EventsCmd;
import com.github.dockerjava.api.model.EventStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.InputStream;

public class EventsCmdExec extends AbstrDockerCmdExec<EventsCmd, EventStream> implements EventsCmd.Exec {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventsCmdExec.class);

    public EventsCmdExec(WebTarget baseResource) {
        super(baseResource);
    }

    @Override
    public EventStream exec(EventsCmd command) {
        WebTarget webResource = getBaseResource().path("/events")
                .queryParam("since", command.getSince())
                .queryParam("until", command.getUntil());

        LOGGER.trace("GET: {}", webResource);
        InputStream inputStream = webResource.request().get(Response.class).readEntity(InputStream.class);
        return EventStream.create(inputStream);
    }
}
