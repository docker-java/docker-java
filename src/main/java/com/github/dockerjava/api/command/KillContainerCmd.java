package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.exception.NotFoundException;

/**
 * Kill a running container.
 */
public interface KillContainerCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    String getContainerId();

    @CheckForNull
    String getSignal();

    KillContainerCmd withContainerId(@Nonnull String containerId);

    KillContainerCmd withSignal(String signal);

    /**
     * @throws NotFoundException
     *             No such container
     */
    @Override
    Void exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<KillContainerCmd, Void> {
    }

}
