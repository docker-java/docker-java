package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.RenameContainerCmd;
import com.github.dockerjava.api.exception.NotFoundException;

import javax.annotation.Nonnull;

import java.util.Objects;

public class RenameContainerCmdImpl extends AbstrDockerCmd<RenameContainerCmd, Void> implements RenameContainerCmd {

    private String containerId;

    private String name;

    public RenameContainerCmdImpl(RenameContainerCmd.Exec exec, String containerId) {
        super(exec);
        withContainerId(containerId);
    }

    @Override
    public String getContainerId() {
        return containerId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public RenameContainerCmd withContainerId(@Nonnull String containerId) {
        this.containerId = Objects.requireNonNull(containerId, "containerId was not specified");
        return this;
    }

    @Override
    public RenameContainerCmd withName(@Nonnull String name) {
        this.name = Objects.requireNonNull(name, "name was not specified");
        return this;
    }

    /**
     * @throws NotFoundException No such container
     */
    @Override
    public Void exec() throws NotFoundException {
        return super.exec();
    }
}
