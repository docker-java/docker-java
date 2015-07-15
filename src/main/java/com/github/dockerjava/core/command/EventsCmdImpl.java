package com.github.dockerjava.core.command;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.EventsCmd;
import com.github.dockerjava.api.model.Event;

/**
 * Stream docker events
 */
public class EventsCmdImpl extends AbstrAsyncDockerCmd<EventsCmd, Event> implements EventsCmd {

    private String since;

    private String until;

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
    public String getSince() {
        return since;
    }

    @Override
    public String getUntil() {
        return until;
    }

    @Override
    public String toString() {
        return new StringBuilder("events").append(since != null ? " --since=" + since : "")
                .append(until != null ? " --until=" + until : "").toString();
    }
}
