package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.ExecStartCmd;
import com.github.dockerjava.api.model.Frame;

public class ExecStartCmdImpl extends AbstrAsyncDockerCmd<ExecStartCmd, Frame> implements ExecStartCmd {

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
     * @throws com.github.dockerjava.api.NotFoundException
     *             No such exec instance
     */
    @Override
    public <T extends ResultCallback<Frame>> T exec(T resultCallback) {
        return super.exec(resultCallback);
    }
}
