package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.EventsCmd;

/**
 * Stream docker events
 */
public class EventsCmdImpl extends AbstrDockerCmd<EventsCmd, Void> implements EventsCmd {

    private String since;
    private String until;
    private EventStreamCallback eventCallback;

    public EventsCmdImpl(EventsCmd.Exec exec, EventStreamCallback eventCallback) {
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
    public EventsCmd withEventCallback(EventStreamCallback eventCallback) {
        this.eventCallback = eventCallback;
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
    public EventStreamCallback getEventCallback() {
        return eventCallback;
    }

//    @Override
//    public InputStream exec() {
//        return super.exec();
//    }

    @Override
    public String toString() {
        return new StringBuilder("events")
                .append(since != null ? " --since=" + since : "")
                .append(until != null ? " --until=" + until : "")
                .toString();
    }
}
