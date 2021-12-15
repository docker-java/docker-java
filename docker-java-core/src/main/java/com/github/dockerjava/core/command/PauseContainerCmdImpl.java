package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.command.PauseContainerCmd;
import com.github.dockerjava.api.exception.NotFoundException;

/**
 * Pause a container.
 *
 * @param containerId
 *            - Id of the container
 *
 */
public class PauseContainerCmdImpl extends AbstrDockerCmd<PauseContainerCmd, Void> implements PauseContainerCmd {

    private String containerId;

    public PauseContainerCmdImpl(PauseContainerCmd.Exec exec, String containerId) {
        super(exec);
        withContainerId(containerId);
    }

    @Override
    public String getContainerId() {
        return containerId;
    }

    @Override
    public PauseContainerCmd withContainerId(String containerId) {
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
