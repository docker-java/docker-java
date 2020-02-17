package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

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
        checkNotNull(containerId, "containerId was not specified");
        this.containerId = containerId;
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
