package com.github.dockerjava.api.command;

import java.util.concurrent.ExecutorService;


/**
 * Get events
 *
 * @param since - Show all events created since timestamp
 * @param until - Stream events until this timestamp
 */
public interface EventsCmd extends DockerCmd<ExecutorService> {
    public EventsCmd withSince(String since);

    public EventsCmd withUntil(String until);

    public String getSince();

    public String getUntil();

    public EventCallback getEventCallback();
    
    public EventsCmd withEventCallback(EventCallback eventCallback);

    public static interface Exec extends DockerCmdExec<EventsCmd, ExecutorService> {
    }
}
