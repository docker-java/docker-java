package com.github.dockerjava.core.command;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.exception.NotModifiedException;

/**
 * Start a container
 */
public class StartContainerCmdImpl extends AbstrDockerCmd<StartContainerCmd, Void> implements StartContainerCmd {

    @JsonIgnore
    private String containerId;

    public StartContainerCmdImpl(StartContainerCmd.Exec exec, String containerId) {
        super(exec);
        withContainerId(containerId);
    }

    @Override
    public StartContainerCmd withContainerId(String containerId) {
        this.containerId = Objects.requireNonNull(containerId, "containerId was not specified");
        return this;
    }

    @Override
    public String getContainerId() {
        return containerId;
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
