package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.NotFoundException;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

public interface ResizeExecCmd extends SyncDockerCmd<Void> {
    @CheckForNull
    String getExecId();

    Integer getHeight();

    Integer getWidth();

    ResizeExecCmd withExecId(@Nonnull String execId);

    ResizeExecCmd withSize(int height, int width);

    /**
     * @throws NotFoundException no such exec instance
     */
    @Override
    Void exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<ResizeExecCmd, Void> {
    }

}
