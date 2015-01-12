package com.github.dockerjava.core.command;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.ExecStartCmd;
import com.google.common.base.Preconditions;

import java.io.InputStream;

public class ExecStartCmdImpl extends AbstrDockerCmd<ExecStartCmd, InputStream> implements ExecStartCmd {

    private String containerId;

    private boolean detach, tty;

    public ExecStartCmdImpl(ExecStartCmd.Exec exec, String containerId) {
        super(exec);
        Preconditions.checkNotNull(containerId, "containerId was not specified");
        this.containerId = containerId;
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

    /**
     * @throws com.github.dockerjava.api.NotFoundException No such container
     */
    @Override
    public InputStream exec() throws NotFoundException {
        return super.exec();
    }
}
