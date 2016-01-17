package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.ListNetworksCmd;
import com.github.dockerjava.api.model.Filters;
import com.github.dockerjava.api.model.Network;

import java.util.List;

public class ListNetworksCmdImpl extends AbstrDockerCmd<ListNetworksCmd, List<Network>> implements
    ListNetworksCmd {

    private Filters filters;

    public ListNetworksCmdImpl(ListNetworksCmd.Exec exec) {
        super(exec);
    }

    @Override public Filters getFilters() {
        return filters;
    }

    @Override public ListNetworksCmd withFilters(Filters filters) {

        this.filters = filters;
        return this;
    }
}
