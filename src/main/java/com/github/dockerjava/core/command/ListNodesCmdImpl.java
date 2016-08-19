package com.github.dockerjava.core.command;


import com.github.dockerjava.api.command.ListNodesCmd;
import com.github.dockerjava.api.model.SwarmNode;
import com.github.dockerjava.core.util.ServiceFiltersBuilder;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * List SwarmNodes
 */
public class ListNodesCmdImpl extends AbstrDockerCmd<ListNodesCmd, List<SwarmNode>> implements
        ListNodesCmd  {

    private ServiceFiltersBuilder filters = new ServiceFiltersBuilder();

    public ListNodesCmdImpl(Exec exec) {
        super(exec);
    }

    @Override
    public Map<String, List<String>> getFilters() {
        return filters.build();
    }

    @Override
    public ListNodesCmd withIdFilter(List<String> ids) {
        checkNotNull(ids, "ids was not specified");
        this.filters.withIds(ids);
        return this;
    }

    @Override
    public ListNodesCmd withNameFilter(List<String> names) {
        checkNotNull(names, "names was not specified");
        this.filters.withNames(names);
        return this;
    }
}
