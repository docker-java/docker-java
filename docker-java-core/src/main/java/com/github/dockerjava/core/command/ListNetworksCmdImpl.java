package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.ListNetworksCmd;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.core.util.FiltersBuilder;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ListNetworksCmdImpl extends AbstrDockerCmd<ListNetworksCmd, List<Network>> implements ListNetworksCmd {

    private FiltersBuilder filtersBuilder = new FiltersBuilder();

    public ListNetworksCmdImpl(ListNetworksCmd.Exec exec) {
        super(exec);
    }

    @Override
    public Map<String, List<String>> getFilters() {
        return filtersBuilder.build();
    }

    @Override
    public ListNetworksCmd withIdFilter(String... networkId) {
        this.filtersBuilder.withFilter("id", networkId);
        return this;
    }

    @Override
    public ListNetworksCmd withNameFilter(String... networkName) {
        this.filtersBuilder.withFilter("name", networkName);
        return this;
    }

    @Override
    public ListNetworksCmd withFilter(String filterName, Collection<String> filterValues) {
        Objects.requireNonNull(filterValues, filterName + " was not specified");
        this.filtersBuilder.withFilter(filterName, filterValues);
        return this;
    }
}
