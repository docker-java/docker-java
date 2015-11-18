package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.NotFoundException;

/**
 * Pause a container.
 *
 * @param containerId
 *            - Id of the container
 *
 */
public interface PauseContainerCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    public String getContainerId();

    public PauseContainerCmd withContainerId(@Nonnull String containerId);

    /**
     * @throws NotFoundException
     *             No such container
     */
    @Override
    public Void exec() throws NotFoundException;

    public static interface Exec extends DockerCmdSyncExec<PauseContainerCmd, Void> {
    }

}