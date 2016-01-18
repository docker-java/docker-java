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
    public Map<String, List<String>> getFilters();

    @CheckForNull
    public String getSince();

    @CheckForNull
    public String getUntil();

    /**
     * @param container
     *            - container to filter
     */
    public EventsCmd withContainerFilter(String... container);

    /**
     * @param event
     *            - event to filter (pull | create | attach | start | stop | kill)
     */
    public EventsCmd withEventFilter(String... event);

    /**
     * @param image
     *            - image to filter
     */
    public EventsCmd withImageFilter(String... image);

    /**
     * @param label
     *            - label to filter
     */
    public EventsCmd withLabelFilter(String... label);

    /**
     * @param labels
     *            - labels to filter (map of names and values)
     */
    public EventsCmd withLabelFilter(Map<String, String> labels);

    /**
     * @param since
     *            - Show all events created since timestamp
     */
    public EventsCmd withSince(String since);

    /**
     * @param until
     *            - Show all events created until timestamp
     */
    public EventsCmd withUntil(String until);

    public static interface Exec extends DockerCmdAsyncExec<EventsCmd, Event> {
    }
}
