package com.github.dockerjava.api.command;

import com.github.dockerjava.core.RemoteApiVersion;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Disconnects a container from a network.
 *
 * @since {@link RemoteApiVersion#VERSION_1_21}
 */
public interface DisconnectFromNetworkCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    public String getNetworkId();

    @CheckForNull
    public String getContainerId();

    public DisconnectFromNetworkCmd withNetworkId(@Nonnull String networkId);

    public DisconnectFromNetworkCmd withContainerId(@Nonnull String containerId);

    public static interface Exec extends DockerCmdSyncExec<DisconnectFromNetworkCmd, Void> {
    }
}
