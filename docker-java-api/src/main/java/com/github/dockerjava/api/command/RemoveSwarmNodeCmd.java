package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.NotFoundException;

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

    RemoveSwarmNodeCmd withSwarmNodeId(@Nonnull String swarmNodeId);

    RemoveSwarmNodeCmd withForce(Boolean force);

    /**
     * @throws NotFoundException No such swarmNode
     */
    @Override
    Void exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<RemoveSwarmNodeCmd, Void> {
    }
}
