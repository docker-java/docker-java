package com.github.dockerjava.core.command;

import java.util.Objects;

import com.github.dockerjava.api.command.UnpauseContainerCmd;
import com.github.dockerjava.api.exception.NotFoundException;

/**
 * Unpause a container.
 *
 * @param containerId
 *            - Id of the container
 *
 */
public class UnpauseContainerCmdImpl extends AbstrDockerCmd<UnpauseContainerCmd, Void> implements UnpauseContainerCmd {

    private String containerId;

    public UnpauseContainerCmdImpl(UnpauseContainerCmd.Exec exec, String containerId) {
        super(exec);
        withContainerId(containerId);
    }

    @Override
    public String getContainerId() {
        return containerId;
    }

    @Override
    public UnpauseContainerCmd withContainerId(String containerId) {
        this.containerId = Objects.requireNonNull(containerId, "containerId was not specified");
        return this;
    }

    /**
     * @throws NotFoundException
     *             No such container
     */
    @Override
    public Void exec() throws NotFoundException {
        return super.exec();
    }
}
