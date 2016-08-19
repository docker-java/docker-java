package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.SwarmNode;

import javax.annotation.CheckForNull;
import java.util.List;
import java.util.Map;

/**
 * List SwarmNodes
 */
public interface ListNodesCmd extends SyncDockerCmd<List<SwarmNode>> {

    @CheckForNull
    Map<String, List<String>> getFilters();

    /**
     * @param ids - Show only services with the given ids
     */
    ListNodesCmd withIdFilter(List<String> ids);

    /**
     * @param names - Show only services with the given names
     */
    ListNodesCmd withNameFilter(List<String> names);

    interface Exec extends DockerCmdSyncExec<ListNodesCmd, List<SwarmNode>> {
    }

}
