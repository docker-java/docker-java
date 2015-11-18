package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.NotFoundException;

public interface InspectExecCmd extends SyncDockerCmd<InspectExecResponse> {

    @CheckForNull
    public String getExecId();

    public InspectExecCmd withExecId(@Nonnull String execId);

    /**
     * @throws NotFoundException
     *             if no such exec has been found
     */
    @Override
    public InspectExecResponse exec() throws NotFoundException;

    public static interface Exec extends DockerCmdSyncExec<InspectExecCmd, InspectExecResponse> {
    }
}
