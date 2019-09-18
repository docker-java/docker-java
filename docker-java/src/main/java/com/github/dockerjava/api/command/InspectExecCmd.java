package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.exception.NotFoundException;

public interface InspectExecCmd extends SyncDockerCmd<InspectExecResponse> {

    @CheckForNull
    String getExecId();

    InspectExecCmd withExecId(@Nonnull String execId);

    /**
     * @throws NotFoundException
     *             if no such exec has been found
     */
    @Override
    InspectExecResponse exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<InspectExecCmd, InspectExecResponse> {
    }
}
