package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.ListServicesCmd;
import com.github.dockerjava.api.model.Service;
import com.github.dockerjava.core.util.FiltersBuilder;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * List services.
 */
public class ListServicesCmdImpl extends AbstrDockerCmd<ListServicesCmd, List<Service>> implements
        ListServicesCmd {

    private FiltersBuilder filters = new FiltersBuilder();

    public ListServicesCmdImpl(Exec exec) {
        super(exec);
    }

    @Override
    public Map<String, List<String>> getFilters() {
        return filters.build();
    }

    @Override
    public ListServicesCmd withIdFilter(List<String> ids) {
        checkNotNull(ids, "ids was not specified");
        this.filters.withFilter("id", ids);
        return this;
    }

    @Override
    public ListServicesCmd withNameFilter(List<String> names) {
        checkNotNull(names, "names was not specified");
        this.filters.withFilter("name", names);
        return this;
    }

    @Override
    public ListServicesCmd withLabelFilter(Map<String, String> labels) {
        checkNotNull(labels, "labels was not specified");
        this.filters.withLabels(labels);
        return this;
    }
}
