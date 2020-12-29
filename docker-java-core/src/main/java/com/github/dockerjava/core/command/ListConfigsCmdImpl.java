package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.ListConfigsCmd;
import com.github.dockerjava.api.model.Config;
import com.github.dockerjava.core.util.FiltersBuilder;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * List configs.
 */
public class ListConfigsCmdImpl extends AbstrDockerCmd<ListConfigsCmd, List<Config>> implements
        ListConfigsCmd {

    private FiltersBuilder filters = new FiltersBuilder();

    public ListConfigsCmdImpl(Exec exec) {
        super(exec);
    }

    @Override
    public Map<String, List<String>> getFilters() {
        return filters.build();
    }

    @Override
    public ListConfigsCmd withIdFilter(List<String> ids) {
        checkNotNull(ids, "ids was not specified");
        this.filters.withFilter("id", ids);
        return this;
    }

    @Override
    public ListConfigsCmd withNameFilter(List<String> names) {
        checkNotNull(names, "names was not specified");
        this.filters.withFilter("name", names);
        return this;
    }

    @Override
    public ListConfigsCmd withLabelFilter(Map<String, String> labels) {
        checkNotNull(labels, "labels was not specified");
        this.filters.withLabels(labels);
        return this;
    }
}
