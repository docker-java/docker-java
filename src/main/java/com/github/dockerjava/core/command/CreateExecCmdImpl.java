package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.command.CreateExecCmd;
import com.github.dockerjava.api.command.CreateExecCmdResponse;
import com.github.dockerjava.api.exception.NotFoundException;

@JsonInclude(Include.NON_NULL)
public class CreateExecCmdImpl extends AbstrDockerCmd<CreateExecCmd, CreateExecCmdResponse> implements CreateExecCmd {

    private String containerId;

    @JsonProperty("AttachStdin")
    private Boolean attachStdin;

    @JsonProperty("AttachStdout")
    private Boolean attachStdout;

    @JsonProperty("AttachStderr")
    private Boolean attachStderr;

    @JsonProperty("Tty")
    private Boolean tty;

    @JsonProperty("Cmd")
    private String[] cmd;

    public CreateExecCmdImpl(CreateExecCmd.Exec exec, String containerId) {
        super(exec);
        withContainerId(containerId);
    }

    @Override
    public CreateExecCmd withContainerId(String containerId) {
        checkNotNull(containerId, "containerId was not specified");
        this.containerId = containerId;
        return this;
    }

    @Override
    public CreateExecCmd withAttachStdin(Boolean attachStdin) {
        this.attachStdin = attachStdin;
        return this;
    }

    @Override
    public CreateExecCmd withAttachStdout(Boolean attachStdout) {
        this.attachStdout = attachStdout;
        return this;
    }

    @Override
    public CreateExecCmd withAttachStderr(Boolean attachStderr) {
        this.attachStderr = attachStderr;
        return this;
    }

    @Override
    public CreateExecCmd withTty(Boolean tty) {
        this.tty = tty;
        return this;
    }

    @Override
    public CreateExecCmd withCmd(String... cmd) {
        this.cmd = cmd;
        return this;
    }

    @Override
    public String getContainerId() {
        return containerId;
    }

    @Override
    public Boolean hasAttachStdinEnabled() {
        return attachStdin;
    }

    @Override
    public Boolean hasAttachStdoutEnabled() {
        return attachStdout;
    }

    @Override
    public Boolean hasAttachStderrEnabled() {
        return attachStderr;
    }

    @Override
    public Boolean hasTtyEnabled() {
        return tty;
    }

    /**
     * @throws NotFoundException
     *             No such container
     */
    @Override
    public CreateExecCmdResponse exec() throws NotFoundException {
        return super.exec();
    }

}
