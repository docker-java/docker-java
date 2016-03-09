package com.github.dockerjava.api.command;

import java.io.InputStream;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Frame;

public interface ExecStartCmd extends AsyncDockerCmd<ExecStartCmd, Frame> {

    @CheckForNull
    String getExecId();

    @CheckForNull
    Boolean hasDetachEnabled();

    @CheckForNull
    Boolean hasTtyEnabled();

    @CheckForNull
    InputStream getStdin();

    ExecStartCmd withDetach(Boolean detach);

    ExecStartCmd withExecId(@Nonnull String execId);

    ExecStartCmd withTty(Boolean tty);

    ExecStartCmd withStdIn(InputStream stdin);

    /**
     *
     * @throws NotFoundException
     *             No such exec instance
     */
    @Override
    <T extends ResultCallback<Frame>> T exec(T resultCallback);

    interface Exec extends DockerCmdAsyncExec<ExecStartCmd, Frame> {
    }

}
