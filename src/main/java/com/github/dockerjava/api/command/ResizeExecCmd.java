package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.exception.NotFoundException;

public interface ResizeExecCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    String getExecId();

    Integer getHeight();

    Integer getWidth();

    ResizeExecCmd withExecId(@Nonnull String execId);

    ResizeExecCmd withSize(int height, int width);

    /**
     *
     * @throws NotFoundException
     *             No such exec instance
     */
    @Override
    Void exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<ResizeExecCmd, Void> {
    }

}
