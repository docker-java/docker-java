package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.ResizeContainerCmd;
import com.github.dockerjava.api.exception.NotFoundException;

import java.util.Objects;

public class ResizeContainerCmdImpl extends AbstrDockerCmd<ResizeContainerCmd, Void> implements ResizeContainerCmd {

    private String containerId;

    private Integer height;

    private Integer width;

    public ResizeContainerCmdImpl(ResizeContainerCmd.Exec exec, String execId) {
        super(exec);
        withContainerId(execId);
    }

    @Override
    public String getContainerId() {
        return containerId;
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
    public ResizeContainerCmd withContainerId(String containerId) {
        this.containerId = Objects.requireNonNull(containerId, "containerId was not specified");
        return this;
    }

    @Override
    public ResizeContainerCmd withSize(int height, int width) {
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
