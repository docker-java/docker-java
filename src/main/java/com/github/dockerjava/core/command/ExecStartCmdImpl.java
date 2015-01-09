package com.github.dockerjava.core.command;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.ExecStartCmd;
import com.google.common.base.Preconditions;

import java.io.InputStream;

public class ExecStartCmdImpl extends	AbstrDockerCmd<ExecStartCmd, InputStream> implements ExecStartCmd {

    private String containerId;

    private boolean detach, tty;

    public ExecStartCmdImpl(ExecStartCmd.Exec exec, String containerId) {
        super(exec);
        withContainerId(containerId);
    }

    @Override
    public String getContainerId() {
        return containerId;
    }

    @Override
    public boolean isDetach() {
        return detach;
    }

    @Override
    public boolean isTty() {
        return tty;
    }

    @Override
    public ExecStartCmdImpl withContainerId(String containerId) {
        Preconditions.checkNotNull(containerId, "containerId was not specified");
        this.containerId = containerId;
        return this;
    }

    /**
     * @throws com.github.dockerjava.api.NotFoundException No such container
     */
    @Override
    public InputStream exec() throws NotFoundException {
        return super.exec();
    }
}
