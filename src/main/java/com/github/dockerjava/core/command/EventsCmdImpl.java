package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.*;

import java.util.concurrent.ExecutorService;

import com.github.dockerjava.api.command.EventCallback;
import com.github.dockerjava.api.command.EventsCmd;

/**
 * Stream docker events
 */
public class EventsCmdImpl extends AbstrDockerCmd<EventsCmd, ExecutorService> implements EventsCmd {

    private String since;

    private String until;

    private EventCallback eventCallback;

    private String filters;

    public EventsCmdImpl(EventsCmd.Exec exec, EventCallback eventCallback) {
        super(exec);
        withEventCallback(eventCallback);
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
    public EventsCmd withEventCallback(EventCallback eventCallback) {
        this.eventCallback = eventCallback;
        return this;
    }

    @Override
    public EventsCmd withFilters(String filters) {
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
    public EventCallback getEventCallback() {
        return eventCallback;
    }

    @Override
    public String getFilters() {
        return filters;
    }

    @Override
    public ExecutorService exec() {
        return super.exec();
    }

    @Override
    public String toString() {
        return new StringBuilder("events").append(since != null ? " --since=" + since : "")
                .append(until != null ? " --until=" + until : "")
                .append(filters != null ? " --filters=" + filters : "").toString();
    }
}
