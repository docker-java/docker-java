package com.github.dockerjava.api.command;

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
    public EventsCmd withSince(String since);

    public EventsCmd withUntil(String until);

    public String getSince();

    public String getUntil();

    public Filters getFilters();

    public EventsCmd withFilters(Filters filters);

    public static interface Exec extends DockerCmdAsyncExec<EventsCmd, Event> {

    }
}
