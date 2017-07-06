package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.exception.NotFoundException;

public interface ResizeContainerCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    String getContainerId();

    Integer getHeight();

    Integer getWidth();

    ResizeContainerCmd withContainerId(@Nonnull String execId);

    ResizeContainerCmd withSize(int height, int width);

    /**
     *
     * @throws NotFoundException
     *             No such container instance
     */
    @Override
    Void exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<ResizeContainerCmd, Void> {
    }

}
