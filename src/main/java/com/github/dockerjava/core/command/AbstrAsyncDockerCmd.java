package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.AsyncDockerCmd;
import com.github.dockerjava.api.command.DockerCmdAsyncExec;

public abstract class AbstrAsyncDockerCmd<CMD_T extends AsyncDockerCmd<CMD_T, A_RES_T>, A_RES_T> implements
        AsyncDockerCmd<CMD_T, A_RES_T> {

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstrAsyncDockerCmd.class);

    protected DockerCmdAsyncExec<CMD_T, A_RES_T> execution;

    public AbstrAsyncDockerCmd(DockerCmdAsyncExec<CMD_T, A_RES_T> execution) {
        checkNotNull(execution, "execution was not specified");
        this.execution = execution;
    }

    @Override
    public <T extends ResultCallback<A_RES_T>> T exec(T resultCallback) {
        execution.exec((CMD_T) this, resultCallback);
        return resultCallback;
    }

    @Override
    public void close() throws IOException {
    }



}
