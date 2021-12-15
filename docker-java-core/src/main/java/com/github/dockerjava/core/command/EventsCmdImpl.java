package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;

import com.github.dockerjava.api.command.EventsCmd;
import com.github.dockerjava.api.model.Event;
import com.github.dockerjava.core.util.FiltersBuilder;

/**
 * Stream docker events
 */
public class EventsCmdImpl extends AbstrAsyncDockerCmd<EventsCmd, Event> implements EventsCmd {

    private String since;

    private String until;

    private FiltersBuilder filters = new FiltersBuilder();

    public EventsCmdImpl(EventsCmd.Exec exec) {
        super(exec);
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
    public EventsCmd withContainerFilter(String... container) {
        checkNotNull(container, "container have not been specified");
        this.filters.withContainers(container);
        return this;
    }

    @Override
    public EventsCmd withImageFilter(String... image) {
        checkNotNull(image, "image have not been specified");
        this.filters.withImages(image);
        return this;
    }

    @Override
    public EventsCmd withEventFilter(String... event) {
        checkNotNull(event, "event have not been specified");
        this.filters.withFilter("event", event);
        return this;
    }

    @Override
    public EventsCmd withEventTypeFilter(String... eventTypes) {
        checkNotNull(eventTypes, "event types have not been specified");
        this.filters.withEventTypes(eventTypes);
        return this;
    }

    @Override
    public EventsCmd withLabelFilter(String... label) {
        checkNotNull(label, "label have not been specified");
        this.filters.withLabels(label);
        return this;
    }

    @Override
    public EventsCmd withLabelFilter(Map<String, String> labels) {
        checkNotNull(labels, "labels have not been specified");
        this.filters.withLabels(labels);
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
    public Map<String, List<String>> getFilters() {
        return filters.build();
    }

}
