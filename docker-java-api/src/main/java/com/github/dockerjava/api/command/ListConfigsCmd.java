package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.Config;

import java.util.List;
import java.util.Map;

/**
 * Command to list all configs in a docker swarm. Only applicable if docker runs in swarm mode.
 *
 * @since {@link RemoteApiVersion#VERSION_1_30}
 */
public interface ListConfigsCmd extends SyncDockerCmd<List<Config>> {

    Map<String, List<String>> getFilters();

    ListConfigsCmd withFilters(Map<String, List<String>> filters);

    interface Exec extends DockerCmdSyncExec<ListConfigsCmd, List<Config>> {
    }
}
