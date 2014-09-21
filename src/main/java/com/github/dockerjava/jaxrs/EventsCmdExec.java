package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.EventsCmd;
import com.github.dockerjava.api.model.EventNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;

public class EventsCmdExec extends AbstrDockerCmdExec<EventsCmd, Void> implements EventsCmd.Exec {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventsCmdExec.class);

    public EventsCmdExec(WebTarget baseResource) {
        super(baseResource);
    }

    @Override
    protected Void execute(EventsCmd command) {
        WebTarget webResource = getBaseResource().path("/events")
                .queryParam("since", command.getSince())
                .queryParam("until", command.getUntil());

        LOGGER.trace("GET: {}", webResource);
        EventNotifier eventNotifier = EventNotifier.create(command.getEventCallback(), webResource);
        command.getExecutorService().submit(eventNotifier);
        return null;
    }
}
