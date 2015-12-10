package com.github.dockerjava.api.command;

import java.io.InputStream;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Frame;

public interface ExecStartCmd extends AsyncDockerCmd<ExecStartCmd, Frame> {

    @CheckForNull
    public String getExecId();

    @CheckForNull
    public Boolean hasDetachEnabled();

    @CheckForNull
    public Boolean hasTtyEnabled();

    @CheckForNull
    public InputStream getStdin();

    public ExecStartCmd withDetach(Boolean detach);

    public ExecStartCmd withExecId(@Nonnull String execId);

    public ExecStartCmd withTty(Boolean tty);

    public ExecStartCmd withStdIn(InputStream stdin);

    /**
     *
     * @throws NotFoundException
     *             No such exec instance
     */
    @Override
    public <T extends ResultCallback<Frame>> T exec(T resultCallback);

    public static interface Exec extends DockerCmdAsyncExec<ExecStartCmd, Frame> {
    }

}
