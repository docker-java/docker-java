package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.exception.NotModifiedException;

/**
 * Start a container.
 *
 */
public interface StartContainerCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    String getContainerId();

    StartContainerCmd withContainerId(@Nonnull String containerId);

    /**
     * @throws NotFoundException
     *             No such container
     * @throws NotModifiedException
     *             Container already started
     */
    @Override
    Void exec() throws NotFoundException, NotModifiedException;

    interface Exec extends DockerCmdSyncExec<StartContainerCmd, Void> {
    }
}
