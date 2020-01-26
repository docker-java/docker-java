package com.github.dockerjava.api.command;

import javax.annotation.Nonnull;

import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.exception.NotModifiedException;

/**
 * Start a container.
 *
 */
@DockerCommand
public interface StartContainerCmd extends StartContainer, SyncDockerCmd<Void> {

    default StartContainerCmd fromSpec(StartContainerSpec spec) {
        return this
                .withContainerId(spec.getContainerId());
    }

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
