package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.exception.NotFoundException;

/**
 * Restart a running container.
 *
 * @param timeout
 *            - Timeout in seconds before killing the container. Defaults to 10 seconds.
 *
 */
public interface RestartContainerCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    public String getContainerId();

    @CheckForNull
    public Integer getTimeout();

    public RestartContainerCmd withContainerId(@Nonnull String containerId);

    public RestartContainerCmd withtTimeout(Integer timeout);

    /**
     * @throws NotFoundException
     *             No such container
     */
    @Override
    public Void exec() throws NotFoundException;

    public static interface Exec extends DockerCmdSyncExec<RestartContainerCmd, Void> {
    }

}
