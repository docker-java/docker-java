package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;

import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.command.ExecStartCmd;

public class ExecStartCmdImpl extends AbstrDockerCmd<ExecStartCmd, InputStream> implements ExecStartCmd {

    private String execId;

    private Boolean detach, tty;

    public ExecStartCmdImpl(ExecStartCmd.Exec exec, String execId) {
        super(exec);
        withExecId(execId);
    }

    @Override
    public String getExecId() {
        return execId;
    }

    @Override
    public ExecStartCmd withExecId(String execId) {
        checkNotNull(execId, "execId was not specified");
        this.execId = execId;
        return this;
    }

    @Override
    public Boolean hasDetachEnabled() {
        return detach;
    }

    @Override
    public Boolean hasTtyEnabled() {
        return tty;
    }

    @Override
    public ExecStartCmd withDetach(Boolean detach) {
        this.detach = detach;
        return this;
    }

    @Override
    public ExecStartCmd withTty(Boolean tty) {
        this.tty = tty;
        return this;
    }

    /**
     * @throws NotFoundException
     *             No such exec instance
     */
    @Override
    public InputStream exec() throws NotFoundException {
        return super.exec();
    }

}
