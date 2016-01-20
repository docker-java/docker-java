package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;

import com.github.dockerjava.api.NotFoundException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.ExecStartCmd;
import com.github.dockerjava.api.model.Frame;

@JsonInclude(Include.NON_NULL)
public class ExecStartCmdImpl extends AbstrAsyncDockerCmd<ExecStartCmd, Frame> implements ExecStartCmd {

    @JsonIgnore
    private String execId;

    @JsonProperty("Detach")
    private Boolean detach;

    @JsonProperty("Tty")
    private Boolean tty;


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
     * @throws com.github.dockerjava.api.NotFoundException
     *             No such exec instance
     */
    @Override
    public <T extends ResultCallback<Frame>> T exec(T resultCallback) {
        return super.exec(resultCallback);
    }

}
