package com.github.dockerjava.core.command;

import java.util.Objects;

import com.github.dockerjava.api.command.ResizeExecCmd;
import com.github.dockerjava.api.exception.NotFoundException;

public class ResizeExecCmdImpl extends AbstrDockerCmd<ResizeExecCmd, Void> implements ResizeExecCmd {

    private String execId;

    private Integer height;

    private Integer width;

    public ResizeExecCmdImpl(ResizeExecCmd.Exec exec, String execId) {
        super(exec);
        withExecId(execId);
    }

    @Override
    public String getExecId() {
        return execId;
    }

    @Override
    public Integer getHeight() {
        return height;
    }

    @Override
    public Integer getWidth() {
        return width;
    }

    @Override
    public ResizeExecCmd withExecId(String execId) {
        this.execId = Objects.requireNonNull(execId, "execId was not specified");
        return this;
    }

    @Override
    public ResizeExecCmd withSize(int height, int width) {
        this.height = height;
        this.width = width;
        return this;
    }

    /**
     * @throws NotFoundException no such exec instance
     */
    @Override
    public Void exec() throws NotFoundException {
        return super.exec();
    }

}
