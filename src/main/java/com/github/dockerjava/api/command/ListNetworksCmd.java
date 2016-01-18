package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.Filters;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.core.RemoteApiVersion;

import javax.annotation.CheckForNull;

import java.util.List;

/**
 * List networks.
 *
 * @since {@link RemoteApiVersion#VERSION_1_21}
 */
public interface ListNetworksCmd extends SyncDockerCmd<List<Network>> {

    @CheckForNull
    public Filters getFilters();

    ListNetworksCmd withFilters(Filters filters);

    public static interface Exec extends DockerCmdSyncExec<ListNetworksCmd, List<Network>> {
    }
}
