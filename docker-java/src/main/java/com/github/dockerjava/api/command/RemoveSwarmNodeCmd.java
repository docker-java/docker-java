package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.core.RemoteApiVersion;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Remove a swarmNode.
 *
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public interface RemoveSwarmNodeCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    String getSwarmNodeId();

    @CheckForNull
    Boolean hasForceEnabled();

    RemoveSwarmNodeCmd withContainerId(@Nonnull String containerId);

    RemoveSwarmNodeCmd withForce(Boolean force);

    /**
     * @throws NotFoundException No such swarmNode
     */
    @Override
    Void exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<RemoveSwarmNodeCmd, Void> {
    }
}
