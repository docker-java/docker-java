package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.core.RemoteApiVersion;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Remove a network.
 *
 * @since {@link RemoteApiVersion#VERSION_1_21}
 */
public interface RemoveNetworkCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    public String getNetworkId();

    public RemoveNetworkCmd withNetworkId(@Nonnull String networkId);

    /**
     * @throws NotFoundException
     *             No such network
     */
    @Override
    public Void exec() throws NotFoundException;

    public static interface Exec extends DockerCmdSyncExec<RemoveNetworkCmd, Void> {
    }
}
