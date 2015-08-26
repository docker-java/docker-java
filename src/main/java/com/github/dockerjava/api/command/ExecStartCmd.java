package com.github.dockerjava.api.command;

import java.io.InputStream;

import com.github.dockerjava.api.NotFoundException;

public interface ExecStartCmd extends SyncDockerCmd<InputStream> {

    public String getExecId();

    public ExecStartCmd withExecId(String execId);

    public Boolean hasDetachEnabled();

    public ExecStartCmd withDetach(Boolean detach);

    public ExecStartCmd withDetach();

    public Boolean hasTtyEnabled();

    public ExecStartCmd withTty(Boolean tty);

    public ExecStartCmd withTty();

    /**
     * Its the responsibility of the caller to consume and/or close the {@link InputStream} to prevent connection leaks.
     *
     * @throws com.github.dockerjava.api.NotFoundException
     *             No such exec instance
     */
    @Override
    public InputStream exec() throws NotFoundException;

    public static interface Exec extends DockerCmdSyncExec<ExecStartCmd, InputStream> {
    }
}
