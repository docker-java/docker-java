package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;

import com.github.dockerjava.api.model.Event;
import com.github.dockerjava.api.model.Filters;

/**
 * Get events
 *
 * @param since
 *            - Show all events created since timestamp
 * @param until
 *            - Stream events until this timestamp
 */
public interface EventsCmd extends AsyncDockerCmd<EventsCmd, Event> {

    @CheckForNull
    public Filters getFilters();

    @CheckForNull
    public String getSince();

    @CheckForNull
    public String getUntil();

    public EventsCmd withFilters(Filters filters);

    public EventsCmd withSince(String since);

    public EventsCmd withUntil(String until);

    public static interface Exec extends DockerCmdAsyncExec<EventsCmd, Event> {
    }
}
