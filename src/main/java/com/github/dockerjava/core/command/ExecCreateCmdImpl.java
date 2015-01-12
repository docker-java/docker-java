package com.github.dockerjava.core.command;

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
        this.containerId = containerId;
    }

    public ExecCreateCmd attachStdin(boolean attachStdin) {
        this.attachStdin = attachStdin;
        return this;
    }

    public ExecCreateCmd attachStdout(boolean attachStdout) {
        this.attachStdout = attachStdout;
        return this;
    }

    public ExecCreateCmd tty(boolean tty) {
        this.tty = tty;
        return this;
    }

    public ExecCreateCmd attachStderr(boolean attachStderr) {
        this.attachStderr = attachStderr;
        return this;
    }

    public ExecCreateCmd withCmd(String... cmd) {
        this.cmd = cmd;
        return this;
    }

    @Override
    public String getContainerId() {
        return containerId;
    }

    /**
     * @throws NotFoundException No such container
     */
    @Override
    public ExecCreateCmdResponse exec() throws NotFoundException {
        return super.exec();
    }
}
