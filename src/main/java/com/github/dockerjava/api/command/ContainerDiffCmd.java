package com.github.dockerjava.api.command;

import java.util.List;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.InternalServerErrorException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.ChangeLog;

public interface ContainerDiffCmd extends SyncDockerCmd<List<ChangeLog>> {

    @CheckForNull
    String getContainerId();

    ContainerDiffCmd withContainerId(@Nonnull String containerId);

    @Override
    String toString();

    /**
     * @throws NotFoundException
     *             No such container
     * @throws InternalServerErrorException
     *             server error
     * @throws DockerException
     *             unexpected http status code
     */
    @Override
    List<ChangeLog> exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<ContainerDiffCmd, List<ChangeLog>> {
    }

}
