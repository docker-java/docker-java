package com.github.dockerjava.api.command;

import java.util.List;
import java.util.Map;

import javax.annotation.CheckForNull;

import com.github.dockerjava.api.model.Event;

/**
 * Get events
 */
public interface EventsCmd extends AsyncDockerCmd<EventsCmd, Event> {

    @CheckForNull
    Map<String, List<String>> getFilters();

    @CheckForNull
    String getSince();

    @CheckForNull
    String getUntil();

    /**
     * @param container
     *            - container to filter
     */
    EventsCmd withContainerFilter(String... container);

    /**
     * @param event
     *            - event to filter (pull | create | attach | start | stop | kill)
     */
    EventsCmd withEventFilter(String... event);

    /**
     * @param image
     *            - image to filter
     */
    EventsCmd withImageFilter(String... image);

    /**
     * @param label
     *            - label to filter
     */
    EventsCmd withLabelFilter(String... label);

    /**
     * @param labels
     *            - labels to filter (map of names and values)
     */
    EventsCmd withLabelFilter(Map<String, String> labels);

    /**
     * @param since
     *            - Show all events created since timestamp
     */
    EventsCmd withSince(String since);

    /**
     * @param until
     *            - Show all events created until timestamp
     */
    EventsCmd withUntil(String until);

    interface Exec extends DockerCmdAsyncExec<EventsCmd, Event> {
    }
}
