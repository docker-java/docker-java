package com.github.dockerjava.core.command;

import static jersey.repackaged.com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.ExecStartCmd;

public class ExecStartCmdImpl extends AbstrDockerCmd<ExecStartCmd, InputStream> implements ExecStartCmd {

    private String execId;

    private boolean detach, tty;

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
		return this;
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
