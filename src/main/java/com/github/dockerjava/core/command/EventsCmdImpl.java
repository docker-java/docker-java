package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.command.EventsCmd;
import com.github.dockerjava.api.model.Event;
import com.github.dockerjava.api.model.Filters;

/**
 * Stream docker events
 */
public class EventsCmdImpl extends AbstrAsyncDockerCmd<EventsCmd, Event> implements EventsCmd {

    private String since;

    private String until;

    private Filters filters;

    public EventsCmdImpl(EventsCmd.Exec exec) {
        super(exec);
    }

    @Override
    public EventsCmd withSince(String since) {
        this.since = since;
        return this;
    }

    @Override
    public EventsCmd withUntil(String until) {
        this.until = until;
        return this;
    }

    @Override
    public EventsCmd withFilters(Filters filters) {
        checkNotNull(filters, "filters have not been specified");
        this.filters = filters;
        return this;
    }

    @Override
    public String getSince() {
        return since;
    }

    @Override
    public String getUntil() {
        return until;
    }

    @Override
    public Filters getFilters() {
        return filters;
    }

}
