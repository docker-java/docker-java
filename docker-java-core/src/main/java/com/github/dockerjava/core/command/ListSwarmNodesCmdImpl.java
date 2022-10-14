package com.github.dockerjava.core.command;


import com.github.dockerjava.api.command.ListSwarmNodesCmd;
import com.github.dockerjava.api.model.SwarmNode;
import com.github.dockerjava.core.util.SwarmNodesFiltersBuilder;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * List SwarmNodes
 */
public class ListSwarmNodesCmdImpl extends AbstrDockerCmd<ListSwarmNodesCmd, List<SwarmNode>> implements
        ListSwarmNodesCmd {

    private SwarmNodesFiltersBuilder filters = new SwarmNodesFiltersBuilder();

    public ListSwarmNodesCmdImpl(Exec exec) {
        super(exec);
    }

    @Override
    public Map<String, List<String>> getFilters() {
        return filters.build();
    }

    @Override
    public ListSwarmNodesCmd withIdFilter(List<String> ids) {
        Objects.requireNonNull(ids, "ids was not specified");
        this.filters.withIds(ids);
        return this;
    }

    @Override
    public ListSwarmNodesCmd withNameFilter(List<String> names) {
        Objects.requireNonNull(names, "names was not specified");
        this.filters.withNames(names);
        return this;
    }

    @Override
    public ListSwarmNodesCmd withMembershipFilter(List<String> memberships) {
        Objects.requireNonNull(memberships, "memberships was not specified");
        this.filters.withMemberships(memberships);
        return this;
    }

    @Override
    public ListSwarmNodesCmd withRoleFilter(List<String> roles) {
        Objects.requireNonNull(roles, "roles was not specified");
        this.filters.withRoles(roles);
        return this;
    }
}
