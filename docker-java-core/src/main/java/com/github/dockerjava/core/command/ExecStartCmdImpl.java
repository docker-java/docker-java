package com.github.dockerjava.core.command;

import java.io.InputStream;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.ExecStartCmd;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Frame;

public class ExecStartCmdImpl extends AbstrAsyncDockerCmd<ExecStartCmd, Frame> implements ExecStartCmd {

    @JsonIgnore
    private String execId;

    @JsonProperty("Detach")
    private Boolean detach;

    @JsonProperty("Tty")
    private Boolean tty;

    @JsonIgnore
    private InputStream stdin;

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
        this.execId = Objects.requireNonNull(execId, "execId was not specified");
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
    @JsonIgnore
    public InputStream getStdin() {
        return stdin;
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

    @Override
    public ExecStartCmd withStdIn(InputStream stdin) {
        this.stdin = stdin;
        return this;
    }

    /**
     * @throws NotFoundException
     *             No such exec instance
     */
    @Override
    public <T extends ResultCallback<Frame>> T exec(T resultCallback) {
        return super.exec(resultCallback);
    }
}
