package com.github.dockerjava.core.command;

import java.util.Objects;

import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.NotFoundException;

/**
 * Inspect the details of a container.
 */
public class InspectContainerCmdImpl extends AbstrDockerCmd<InspectContainerCmd, InspectContainerResponse> implements
        InspectContainerCmd {

    private String containerId;
    private boolean size;

    public InspectContainerCmdImpl(InspectContainerCmd.Exec exec, String containerId) {
        super(exec);
        withContainerId(containerId);
    }

    @Override
    public String getContainerId() {
        return containerId;
    }

    @Override
    public InspectContainerCmd withContainerId(String containerId) {
        this.containerId = Objects.requireNonNull(containerId, "containerId was not specified");
        return this;
    }

    @Override
    public InspectContainerCmd withSize(Boolean showSize) {
        this.size = showSize;
        return this;
    }

    @Override
    public Boolean getSize() {
        return size;
    }

    /**
     * @throws NotFoundException
     *             No such container
     */
    @Override
    public InspectContainerResponse exec() throws NotFoundException {
        return super.exec();
    }
}
