package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.NotFoundException;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Available since API v1.21.
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
