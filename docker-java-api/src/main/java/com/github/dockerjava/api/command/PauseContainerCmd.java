package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.exception.NotFoundException;

/**
 * Pause a container.
 *
 * @param containerId
 *            - Id of the container
 *
 */
public interface PauseContainerCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    String getContainerId();

    PauseContainerCmd withContainerId(@Nonnull String containerId);

    /**
     * @throws NotFoundException
     *             No such container
     */
    @Override
    Void exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<PauseContainerCmd, Void> {
    }

}
