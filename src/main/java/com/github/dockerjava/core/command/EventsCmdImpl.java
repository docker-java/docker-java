package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.EventsCmd;
import com.github.dockerjava.api.model.EventStream;

import java.io.InputStream;

/**
 * Stream docker events
 */
public class EventsCmdImpl extends AbstrDockerCmd<EventsCmd, EventStream> implements EventsCmd {

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

    public String getSince() {
        return since;
    }

    public String getUntil() {
        return until;
    }

    @Override
    public EventStream exec() {
        return super.exec();
    }

    @Override
    public String toString() {
        return new StringBuilder("events")
                .append(since != null ? " --since=" + since : "")
                .append(until != null ? " --until=" + until : "")
                .toString();
    }
}
