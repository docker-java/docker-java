package com.github.dockerjava.core.command;

import java.util.Objects;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.AsyncDockerCmd;
import com.github.dockerjava.api.command.DockerCmdAsyncExec;

public abstract class AbstrAsyncDockerCmd<CMD_T extends AsyncDockerCmd<CMD_T, A_RES_T>, A_RES_T> implements
        AsyncDockerCmd<CMD_T, A_RES_T> {

    protected transient DockerCmdAsyncExec<CMD_T, A_RES_T> execution;

    public AbstrAsyncDockerCmd(DockerCmdAsyncExec<CMD_T, A_RES_T> execution) {
        this.execution = Objects.requireNonNull(execution, "execution was not specified");
    }

    @Override
    public <T extends ResultCallback<A_RES_T>> T exec(T resultCallback) {
        execution.exec((CMD_T) this, resultCallback);
        return resultCallback;
    }

    @Override
    public void close() {
    }

}
