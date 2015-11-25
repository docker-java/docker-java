package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.exception.NotFoundException;

public interface InspectContainerCmd extends SyncDockerCmd<InspectContainerResponse> {

    @CheckForNull
    public String getContainerId();

    public InspectContainerCmd withContainerId(@Nonnull String containerId);

    /**
     * @throws NotFoundException
     *             No such container
     */
    @Override
    public InspectContainerResponse exec() throws NotFoundException;

    public static interface Exec extends DockerCmdSyncExec<InspectContainerCmd, InspectContainerResponse> {
    }
}