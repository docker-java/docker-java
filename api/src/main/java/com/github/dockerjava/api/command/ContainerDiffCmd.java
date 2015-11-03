package com.github.dockerjava.api.command;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.InternalServerErrorException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.model.ChangeLog;

import java.util.List;

public interface ContainerDiffCmd extends SyncDockerCmd<List<ChangeLog>> {

    public String getContainerId();

    public ContainerDiffCmd withContainerId(String containerId);

    @Override
    public String toString();

    /**
     * @throws NotFoundException
     *             No such container
     * @throws InternalServerErrorException
     *             server error
     * @throws DockerException
     *             unexpected http status code
     */
    @Override
    public List<ChangeLog> exec() throws NotFoundException;

    public static interface Exec extends DockerCmdSyncExec<ContainerDiffCmd, List<ChangeLog>> {
    }

}