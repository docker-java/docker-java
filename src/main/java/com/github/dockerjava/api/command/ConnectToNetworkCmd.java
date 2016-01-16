package com.github.dockerjava.api.command;

import com.github.dockerjava.core.RemoteApiVersion;

import javax.annotation.CheckForNull;

/**
 * Connects a container to a network.
 *
 * @since {@link RemoteApiVersion#VERSION_1_21}
 */
public interface ConnectToNetworkCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    public String getNetworkId();

    @CheckForNull
    public String getContainerId();

    public ConnectToNetworkCmd withNetworkId(String networkId);

    public ConnectToNetworkCmd withContainerId(String containerId);

    public static interface Exec extends DockerCmdSyncExec<ConnectToNetworkCmd, Void> {
    }
}
