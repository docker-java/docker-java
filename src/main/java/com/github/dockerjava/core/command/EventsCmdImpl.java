package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.EventCallback;
import com.github.dockerjava.api.command.EventsCmd;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Stream docker events
 */
public class EventsCmdImpl extends AbstrDockerCmd<EventsCmd, Void> implements EventsCmd {

    private final EventCallback eventCallback;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private String since;
    private String until;

    public EventsCmdImpl(EventsCmd.Exec exec, EventCallback eventCallback) {
        super(exec);
        this.eventCallback = eventCallback;
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
    public EventCallback getEventCallback() {
        return eventCallback;
    }

    @Override
    public ExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    public void stop() {
        executorService.shutdown();
    }

    @Override
    public Void exec() {
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
