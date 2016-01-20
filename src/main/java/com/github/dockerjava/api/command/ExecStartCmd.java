package com.github.dockerjava.api.command;

import java.io.InputStream;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Frame;

public interface ExecStartCmd extends AsyncDockerCmd<ExecStartCmd, Frame> {

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
    public <T extends ResultCallback<Frame>> T exec(T resultCallback);

    public static interface Exec extends DockerCmdAsyncExec<ExecStartCmd, Frame> {
    }
}
