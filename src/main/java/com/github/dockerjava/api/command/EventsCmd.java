package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.EventStream;

/**
 * Get events
 *
 * @param since - Show all events created since timestamp
 * @param until - Stream events until this timestamp
 */
public interface EventsCmd extends DockerCmd<EventStream> {
    public EventsCmd withSince(String since);

    public EventsCmd withUntil(String until);

    public String getSince();

    public String getUntil();

    public static interface Exec extends DockerCmdExec<EventsCmd, EventStream> {
    }
}
