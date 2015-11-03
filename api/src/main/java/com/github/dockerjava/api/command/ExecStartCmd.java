package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;

import java.io.InputStream;

public interface ExecStartCmd extends SyncDockerCmd<InputStream> {

    public String getExecId();

    public ExecStartCmd withExecId(String execId);

    public boolean hasDetachEnabled();

    public ExecStartCmd withDetach(boolean detach);

    public ExecStartCmd withDetach();

    public boolean hasTtyEnabled();

    public ExecStartCmd withTty(boolean tty);

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
