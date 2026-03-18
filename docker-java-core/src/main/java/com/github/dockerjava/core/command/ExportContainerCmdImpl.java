package com.github.dockerjava.core.command;

import java.io.InputStream;
import java.util.Objects;

import com.github.dockerjava.api.command.ExportContainerCmd;
import com.github.dockerjava.api.exception.NotFoundException;

/**
 * Export the contents of a container as a tar archive.
 */
public class ExportContainerCmdImpl extends AbstrDockerCmd<ExportContainerCmd, InputStream> implements
        ExportContainerCmd {

    private String containerId;

    public ExportContainerCmdImpl(ExportContainerCmd.Exec exec, String containerId) {
        super(exec);
        withContainerId(containerId);
    }

    @Override
    public String getContainerId() {
        return containerId;
    }

    @Override
    public ExportContainerCmdImpl withContainerId(String containerId) {
        this.containerId = Objects.requireNonNull(containerId, "containerId was not specified");
        return this;
    }

    /**
     * @throws NotFoundException
     *             No such container
     */
    @Override
    public InputStream exec() throws NotFoundException {
        return super.exec();
    }
}
