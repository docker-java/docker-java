package com.github.dockerjava.api.model;

import java.util.List;

/**
 * Representation of a Docker event filter.
 *
 * @author Carlos Sanchez <carlos@apache.org>
 * 
 */
public class EventFilters extends Filters {

    /**
     * Default constructor for the deserialization.
     */
    public EventFilters() {
    }

    /**
     * Constructor.
     * 
     * @param event
     *            event to filter
     */
    public EventFilters(String... event) {
        super();
        withEvent(event);
    }

    public Filters withEvent(String... event) {
        return withFilter("event", event);
    }

    public List<String> getEvent() {
        return getFilter("event");
    }
}
