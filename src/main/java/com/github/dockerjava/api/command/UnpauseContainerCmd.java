package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.exception.NotFoundException;

/**
 * Unpause a container.
 *
 * @param containerId
 *            - Id of the container
 *
 */
public interface UnpauseContainerCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    String getContainerId();

    UnpauseContainerCmd withContainerId(@Nonnull String containerId);

    /**
     * @throws NotFoundException
     *             No such container
     */
    @Override
    Void exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<UnpauseContainerCmd, Void> {
    }

}
