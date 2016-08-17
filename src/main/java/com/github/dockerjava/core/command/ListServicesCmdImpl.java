package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.command.ListServicesCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Service;
import com.github.dockerjava.core.util.FiltersBuilder;
import com.github.dockerjava.core.util.ServiceFiltersBuilder;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.*;

/**
 * List services.
 */
public class ListServicesCmdImpl extends AbstrDockerCmd<ListServicesCmd, List<Service>> implements
        ListServicesCmd {

    private ServiceFiltersBuilder filters = new ServiceFiltersBuilder();

    public ListServicesCmdImpl(Exec exec) {
        super(exec);
    }

    @Override
    public Map<String, List<String>> getFilters() {
        return filters.build();
    }

    @Override
    public ListServicesCmd withIdFilter(String... ids) {
        checkNotNull(ids, "ids was not specified");
        this.filters.withIds(ids);
        return this;
    }

    @Override
    public ListServicesCmd withNameFilter(String... names) {
        checkNotNull(names, "names was not specified");
        this.filters.withNames(names);
        return this;
    }

}
