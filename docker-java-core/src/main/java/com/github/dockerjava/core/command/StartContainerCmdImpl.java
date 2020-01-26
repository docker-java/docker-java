package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.command.StartContainerSpec;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.exception.NotModifiedException;

/**
 * Start a container
 */
public class StartContainerCmdImpl extends AbstrDockerCmd<StartContainerCmd, Void> implements StartContainerCmd {

    private StartContainerSpec spec;

    public StartContainerCmdImpl(StartContainerCmd.Exec exec, String containerId) {
        super(exec);
        this.spec = StartContainerSpec.of(containerId);
    }

    @Override
    public StartContainerCmd fromSpec(StartContainerSpec spec) {
        this.spec = spec;
        return this;
    }

    @Override
    public StartContainerCmd withContainerId(String containerId) {
        checkNotNull(containerId, "containerId was not specified");
        this.spec = spec.withContainerId(containerId);
        return this;
    }

    @Override
    public String getContainerId() {
        return spec.getContainerId();
    }

    /**
     * @throws NotFoundException
     *             No such container
     * @throws NotModifiedException
     *             Container already started
     */
    @Override
    public Void exec() throws NotFoundException, NotModifiedException {
        return super.exec();
    }
}
