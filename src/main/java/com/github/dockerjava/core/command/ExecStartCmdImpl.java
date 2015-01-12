package com.github.dockerjava.core.command;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.ExecStartCmd;
import com.google.common.base.Preconditions;

import java.io.InputStream;

public class ExecStartCmdImpl extends AbstrDockerCmd<ExecStartCmd, InputStream> implements ExecStartCmd {

    private String execId;

    private boolean detach, tty;

    public ExecStartCmdImpl(ExecStartCmd.Exec exec, String containerId) {
        super(exec);
        Preconditions.checkNotNull(containerId, "containerId was not specified");
        this.execId = containerId;
    }

    @Override
    public String getExecId() {
        return execId;
    }
    
    @Override
	public ExecStartCmd withExecId(String execId) {
    	Preconditions.checkNotNull(execId, "execId was not specified");
		this.execId = execId;
		return this;
	}

    @Override
    public boolean hasDetachEnabled() {
        return detach;
    }

    @Override
    public boolean hasTtyEnabled() {
        return tty;
    }

    @Override
	public ExecStartCmd withDetach(boolean detach) {
		this.detach = detach;
		return this;
	}

	@Override
	public ExecStartCmd withTty(boolean tty) {
		this.tty = tty;
		return null;
	}
	
	@Override
	public ExecStartCmd withDetach() {
		return withDetach(true);
	}

	@Override
	public ExecStartCmd withTty() {
		return withTty(true);
	}
    
    /**
     * @throws com.github.dockerjava.api.NotFoundException No such exec instance
     */
    @Override
    public InputStream exec() throws NotFoundException {
        return super.exec();
    }
    
}
