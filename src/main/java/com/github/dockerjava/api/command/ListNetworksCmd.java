package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.core.RemoteApiVersion;

import javax.annotation.CheckForNull;

import java.util.List;
import java.util.Map;

/**
 * List networks.
 *
 * @since {@link RemoteApiVersion#VERSION_1_21}
 */
public interface ListNetworksCmd extends SyncDockerCmd<List<Network>> {

    @CheckForNull
    Map<String, List<String>> getFilters();

    ListNetworksCmd withNameFilter(String... networkName);

    ListNetworksCmd withIdFilter(String... networkId);

    interface Exec extends DockerCmdSyncExec<ListNetworksCmd, List<Network>> {
    }
}
