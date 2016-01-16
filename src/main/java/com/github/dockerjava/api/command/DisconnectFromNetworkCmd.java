package com.github.dockerjava.api.command;

import com.github.dockerjava.core.RemoteApiVersion;

import javax.annotation.CheckForNull;

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

    public DisconnectFromNetworkCmd withNetworkId(String networkId);

    public DisconnectFromNetworkCmd withContainerId(String containerId);

    public static interface Exec extends DockerCmdSyncExec<DisconnectFromNetworkCmd, Void> {
    }
}
