package com.github.dockerjava.core.command;

import static jersey.repackaged.com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.ExecCreateCmd;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;

public class ExecCreateCmdImpl extends AbstrDockerCmd<ExecCreateCmd, ExecCreateCmdResponse> implements ExecCreateCmd {

    private String containerId;

    @JsonProperty("AttachStdin")
    private boolean attachStdin;

    @JsonProperty("AttachStdout")
    private boolean attachStdout;

    @JsonProperty("AttachStderr")
    private boolean attachStderr;

    @JsonProperty("Tty")
    private boolean tty;

    @JsonProperty("Cmd")
    private String[] cmd;

    public ExecCreateCmdImpl(ExecCreateCmd.Exec exec, String containerId) {
        super(exec);
        withContainerId(containerId);
    }
    
    @Override
    public ExecCreateCmd withContainerId(String containerId) {
    	checkNotNull(containerId, "containerId was not specified");
        this.containerId = containerId;
        return this;
    }

    @Override
    public ExecCreateCmd withAttachStdin(boolean attachStdin) {
        this.attachStdin = attachStdin;
        return this;
    }
    
    @Override
    public ExecCreateCmd withAttachStdin() {
        return withAttachStdin(true);
    }

    @Override
    public ExecCreateCmd withAttachStdout(boolean attachStdout) {
        this.attachStdout = attachStdout;
        return this;
    }
    
    @Override
    public ExecCreateCmd withAttachStdout() {
        return withAttachStdout(true);
    }

    @Override
    public ExecCreateCmd withAttachStderr(boolean attachStderr) {
        this.attachStderr = attachStderr;
        return this;
    }
    
    @Override
    public ExecCreateCmd withAttachStderr() {
        return withAttachStderr(true);
    }
    
    @Override
    public ExecCreateCmd withTty(boolean tty) {
        this.tty = tty;
        return this;
    }
    
    @Override
    public ExecCreateCmd withTty() {
        return withTty(true);
    }

    @Override
    public ExecCreateCmd withCmd(String... cmd) {
        this.cmd = cmd;
        return this;
    }

    @Override
    public String getContainerId() {
        return containerId;
    }
    
    @Override
	public boolean hasAttachStdinEnabled() {
		return attachStdin;
	}

	@Override
	public boolean hasAttachStdoutEnabled() {
		return attachStdout;
	}

	@Override
	public boolean hasAttachStderrEnabled() {
		return attachStderr;
	}

	@Override
	public boolean hasTtyEnabled() {
		return tty;
	}

    /**
     * @throws NotFoundException No such container
     */
    @Override
    public ExecCreateCmdResponse exec() throws NotFoundException {
        return super.exec();
    }

	
}
