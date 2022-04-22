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
    String getContainerId();

    @CheckForNull
    Integer getTimeout();

    RestartContainerCmd withContainerId(@Nonnull String containerId);

    /**
     * @deprecated wrong name, use {@link #withTimeout(Integer)}
     */
    @Deprecated
    default RestartContainerCmd withtTimeout(Integer timeout) {
        return withTimeout(timeout);
    }

    RestartContainerCmd withTimeout(Integer timeout);

    /**
     * @throws NotFoundException
     *             No such container
     */
    @Override
    Void exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<RestartContainerCmd, Void> {
    }

}
