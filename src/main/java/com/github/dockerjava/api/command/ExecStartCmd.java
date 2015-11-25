package com.github.dockerjava.api.command;

import java.io.InputStream;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.exception.NotFoundException;

public interface ExecStartCmd extends SyncDockerCmd<InputStream> {

    @CheckForNull
    public String getExecId();

    @CheckForNull
    public Boolean hasDetachEnabled();

    @CheckForNull
    public Boolean hasTtyEnabled();

    public ExecStartCmd withDetach(Boolean detach);

    public ExecStartCmd withExecId(@Nonnull String execId);

    public ExecStartCmd withTty(Boolean tty);

    /**
     * Its the responsibility of the caller to consume and/or close the {@link InputStream} to prevent connection leaks.
     *
     * @throws NotFoundException
     *             No such exec instance
     */
    @Override
    public InputStream exec() throws NotFoundException;

    public static interface Exec extends DockerCmdSyncExec<ExecStartCmd, InputStream> {
    }

}
