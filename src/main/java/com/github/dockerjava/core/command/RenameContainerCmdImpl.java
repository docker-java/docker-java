package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.RenameContainerCmd;
import com.github.dockerjava.api.exception.NotFoundException;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

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
        checkNotNull(containerId, "containerId was not specified");
        this.containerId = containerId;
        return this;
    }

    @Override
    public RenameContainerCmd withName(@Nonnull String name) {
        checkNotNull(name, "name was not specified");
        this.name = name;
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
