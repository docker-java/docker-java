package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.exception.NotModifiedException;

/**
 * Stop a running container.
 *
 * @param containerId
 *            - Id of the container
 * @param timeout
 *            - Timeout in seconds before killing the container. Defaults to 10 seconds.
 *
 */
public interface StopContainerCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    String getContainerId();

    @CheckForNull
    Integer getTimeout();

    StopContainerCmd withContainerId(@Nonnull String containerId);

    StopContainerCmd withTimeout(Integer timeout);

    /**
     * @throws NotFoundException
     *             No such container
     * @throws NotModifiedException
     *             Container already stopped
     */
    @Override
    Void exec() throws NotFoundException, NotModifiedException;

    interface Exec extends DockerCmdSyncExec<StopContainerCmd, Void> {
    }

}
