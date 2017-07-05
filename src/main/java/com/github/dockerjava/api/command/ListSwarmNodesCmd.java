package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.SwarmNode;
import com.github.dockerjava.core.RemoteApiVersion;

import javax.annotation.CheckForNull;
import java.util.List;
import java.util.Map;

/**
 * List SwarmNodes
 *
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public interface ListSwarmNodesCmd extends SyncDockerCmd<List<SwarmNode>> {

    @CheckForNull
    Map<String, List<String>> getFilters();

    /**
     * @param ids - Show only swarmNodes with the given ids
     */
    ListSwarmNodesCmd withIdFilter(List<String> ids);

    /**
     * @param names - Show only swarmNodes with the given names
     */
    ListSwarmNodesCmd withNameFilter(List<String> names);

    /**
     * @param memberships - Show only swarmNodes with the given memberships
     */
    ListSwarmNodesCmd withMembershipFilter(List<String> memberships);

    /**
     * @param roles - Show only swarmNodes with the given roles
     */
    ListSwarmNodesCmd withRoleFilter(List<String> roles);

    interface Exec extends DockerCmdSyncExec<ListSwarmNodesCmd, List<SwarmNode>> {
    }

}
