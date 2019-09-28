package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.InspectExecCmd;
import com.github.dockerjava.api.command.InspectExecResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.google.common.base.Preconditions;

public class InspectExecCmdImpl extends AbstrDockerCmd<InspectExecCmd, InspectExecResponse> implements InspectExecCmd {
    private String execId;

    public InspectExecCmdImpl(InspectExecCmd.Exec execution, String execId) {
        super(execution);
        withExecId(execId);
    }

    @Override
    public String getExecId() {
        return execId;
    }

    @Override
    public InspectExecCmd withExecId(String execId) {
        Preconditions.checkNotNull(execId, "execId was not specified");
        this.execId = execId;
        return this;
    }

    /**
     * @throws NotFoundException
     *             No such exec
     */
    @Override
    public InspectExecResponse exec() throws NotFoundException {
        return super.exec();
    }
}
