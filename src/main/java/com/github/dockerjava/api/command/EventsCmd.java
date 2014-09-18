package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.EventNotifier;

/**
 * Get events
 *
 * @param since - Show all events created since timestamp
 * @param until - Stream events until this timestamp
 */
public interface EventsCmd extends DockerCmd<EventNotifier> {
    public EventsCmd withSince(String since);

    public EventsCmd withUntil(String until);

    public String getSince();

    public String getUntil();

    public EventCallback getEventCallback();

    public static interface Exec extends DockerCmdExec<EventsCmd, EventNotifier> {
    }
}
