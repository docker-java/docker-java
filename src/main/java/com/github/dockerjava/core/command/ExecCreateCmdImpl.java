package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.command.ExecCreateCmd;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.exception.NotFoundException;

@JsonInclude(Include.NON_NULL)
public class ExecCreateCmdImpl extends AbstrDockerCmd<ExecCreateCmd, ExecCreateCmdResponse> implements ExecCreateCmd {

    private String containerId;

    @JsonProperty("AttachStdin")
    private Boolean attachStdin;

    @JsonProperty("AttachStdout")
    private Boolean attachStdout;

    @JsonProperty("AttachStderr")
    private Boolean attachStderr;

    @JsonProperty("Tty")
    private Boolean tty;

    /**
    * @since 1.21
    **/
    @JsonProperty("Privileged")
    private Boolean privileged;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_19}
     */
    @JsonProperty("User")
    private String user;

    @JsonProperty("Cmd")
    private String[] cmd;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_25}
     */
    @JsonProperty("Env")
    private String[] env;

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
    public ExecCreateCmd withAttachStdin(Boolean attachStdin) {
        this.attachStdin = attachStdin;
        return this;
    }

    @Override
    public ExecCreateCmd withAttachStdout(Boolean attachStdout) {
        this.attachStdout = attachStdout;
        return this;
    }

    @Override
    public ExecCreateCmd withAttachStderr(Boolean attachStderr) {
        this.attachStderr = attachStderr;
        return this;
    }

    @Override
    public ExecCreateCmd withTty(Boolean tty) {
        this.tty = tty;
        return this;
    }

    @Override
    public ExecCreateCmd withUser(String user) {
        this.user = user;
        return this;
    }

    @Override
    public ExecCreateCmd withCmd(String... cmd) {
        this.cmd = cmd;
        return this;
    }

    @Override
    public ExecCreateCmd withPrivileged(Boolean privileged) {
        this.privileged = privileged;
        return this;
    }

    @Override
    public ExecCreateCmd withEnv(String... env) {
        this.env = env;
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

    @Override
    public Boolean getPrivileged() {
        return privileged;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String[] getEnv() {
        return env;
    }

    /**
     * @throws NotFoundException
     *             No such container
     */
    @Override
    public ExecCreateCmdResponse exec() throws NotFoundException {
        return super.exec();
    }

}
