package com.github.dockerjava.core.command;

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
    public ExecutorService exec() {
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
