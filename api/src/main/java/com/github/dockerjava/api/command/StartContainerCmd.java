package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.NotModifiedException;

/**
 * Start a container.
 *
 */
public interface StartContainerCmd extends SyncDockerCmd<Void> {

    public static interface Exec extends DockerCmdSyncExec<StartContainerCmd, Void> {
    }

    String getContainerId();

    StartContainerCmd withContainerId(String containerId);

    /**
     * @throws NotFoundException
     *             No such container
     * @throws NotModifiedException
     *             Container already started
     */
    @Override
    public Void exec() throws NotFoundException, NotModifiedException;
}
