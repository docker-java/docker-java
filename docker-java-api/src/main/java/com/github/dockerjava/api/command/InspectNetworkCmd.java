package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.core.RemoteApiVersion;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Inspect a network.
 *
 * @since {@link RemoteApiVersion#VERSION_1_21}
 */
public interface InspectNetworkCmd extends SyncDockerCmd<Network> {

    @CheckForNull
    String getNetworkId();

    InspectNetworkCmd withNetworkId(@Nonnull String networkId);

    /**
     * @throws NotFoundException
     *             No such network
     */
    @Override
    Network exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<InspectNetworkCmd, Network> {
    }
}
