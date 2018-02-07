package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.ListTasksCmd;
import com.github.dockerjava.api.model.Task;
import com.github.dockerjava.api.model.TaskState;
import com.github.dockerjava.core.util.FiltersBuilder;

import javax.annotation.CheckForNull;
import java.util.List;
import java.util.Map;

public class ListTasksCmdImpl extends AbstrDockerCmd<ListTasksCmd, List<Task>> implements ListTasksCmd {
    private FiltersBuilder filters = new FiltersBuilder();

    public ListTasksCmdImpl(Exec exec) {
        super(exec);
    }

    @CheckForNull
    @Override
    public Map<String, List<String>> getFilters() {
        return filters.build();
    }

    @Override
    public ListTasksCmd withLabelFilter(Map<String, String> labels) {
        filters.withLabels(labels);
        return this;
    }

    @Override
    public ListTasksCmd withLabelFilter(String... labels) {
        filters.withLabels(labels);
        return this;
    }

    @Override
    public ListTasksCmd withIdFilter(String... ids) {
        filters.withFilter("id", ids);
        return this;
    }

    @Override
    public ListTasksCmd withNameFilter(String... names) {
        filters.withFilter("name", names);
        return this;
    }

    @Override
    public ListTasksCmd withNodeFilter(String... nodeNames) {
        filters.withFilter("node", nodeNames);
        return this;
    }

    @Override
    public ListTasksCmd withServiceFilter(String... serviceNames) {
        filters.withFilter("service", serviceNames);
        return this;
    }

    @Override
    public ListTasksCmd withStateFilter(TaskState... desiredStates) {
        String[] states = new String[desiredStates.length];
        for (int i = 0; i < desiredStates.length; i++) {
            states[i] = desiredStates[i].getValue();
        }
        filters.withFilter("desired-state", states);
        return this;
    }
}
