package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.NotFoundException;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

public interface ResizeContainerCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    String getContainerId();

    Integer getHeight();

    Integer getWidth();

    ResizeContainerCmd withContainerId(@Nonnull String execId);

    ResizeContainerCmd withSize(int height, int width);

    /**
     * @throws NotFoundException no such container instance
     */
    @Override
    Void exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<ResizeContainerCmd, Void> {
    }

}
