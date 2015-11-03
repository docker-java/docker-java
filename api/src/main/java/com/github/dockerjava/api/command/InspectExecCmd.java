package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;

public interface InspectExecCmd extends SyncDockerCmd<InspectExecResponse> {
    public String getExecId();

    public InspectExecCmd withExecId(String execId);

    /**
     * @throws NotFoundException
     *             if no such exec has been found
     */
    @Override
    public InspectExecResponse exec() throws NotFoundException;

    public static interface Exec extends DockerCmdSyncExec<InspectExecCmd, InspectExecResponse> {
    }
}
