package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.github.dockerjava.api.command.ContainerDiffCmd;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.InternalServerErrorException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.ChangeLog;

/**
 * Inspect changes on a container's filesystem
 *
 * @param containerId
 *            - Id of the container
 *
 */
public class ContainerDiffCmdImpl extends AbstrDockerCmd<ContainerDiffCmd, List<ChangeLog>> implements ContainerDiffCmd {

    private String containerId;

    public ContainerDiffCmdImpl(ContainerDiffCmd.Exec exec, String containerId) {
        super(exec);
        withContainerId(containerId);
    }

    @Override
    public String getContainerId() {
        return containerId;
    }

    @Override
    public ContainerDiffCmdImpl withContainerId(String containerId) {
        checkNotNull(containerId, "containerId was not specified");
        this.containerId = containerId;
        return this;
    }

    /**
     * @throws NotFoundException
     *             No such container
     * @throws InternalServerErrorException
     *             server error
     * @throws DockerException
     *             unexpected http status code
     */
    @Override
    public List<ChangeLog> exec() throws NotFoundException {
        return super.exec();
    }
}
