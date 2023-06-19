package com.github.dockerjava.core.command;

import java.util.Objects;

import com.github.dockerjava.api.command.KillContainerCmd;
import com.github.dockerjava.api.exception.NotFoundException;

/**
 * Kill a running container.
 */
public class KillContainerCmdImpl extends AbstrDockerCmd<KillContainerCmd, Void> implements KillContainerCmd {

    private String containerId, signal;

    public KillContainerCmdImpl(KillContainerCmd.Exec exec, String containerId) {
        super(exec);
        withContainerId(containerId);
    }

    @Override
    public String getContainerId() {
        return containerId;
    }

    @Override
    public String getSignal() {
        return signal;
    }

    @Override
    public KillContainerCmd withContainerId(String containerId) {
        this.containerId = Objects.requireNonNull(containerId, "containerId was not specified");
        return this;
    }

    @Override
    public KillContainerCmd withSignal(String signal) {
        this.signal = Objects.requireNonNull(signal, "signal was not specified");
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
