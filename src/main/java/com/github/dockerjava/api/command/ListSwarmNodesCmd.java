package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.SwarmNode;

import javax.annotation.CheckForNull;
import java.util.List;
import java.util.Map;

/**
 * List SwarmNodes
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

    interface Exec extends DockerCmdSyncExec<ListSwarmNodesCmd, List<SwarmNode>> {
    }

}
