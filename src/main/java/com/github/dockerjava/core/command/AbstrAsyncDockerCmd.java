package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.AsyncDockerCmd;
import com.github.dockerjava.api.command.DockerCmdExec;

public abstract class AbstrAsyncDockerCmd<CMD_T extends AsyncDockerCmd<CMD_T, A_RES_T, RES_T>, A_RES_T, RES_T>
        extends AbstrDockerCmd<CMD_T, RES_T> implements AsyncDockerCmd<CMD_T, A_RES_T, RES_T>{

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstrAsyncDockerCmd.class);

    private ResultCallback<A_RES_T> resultCallback;

    public AbstrAsyncDockerCmd(DockerCmdExec<CMD_T, RES_T> execution, ResultCallback<A_RES_T> resultCallback) {
        super(execution);
        checkNotNull(resultCallback, "resultCallback was not specified");
        withResultCallback(resultCallback);
    }

    @SuppressWarnings("unchecked")
    @Override
    public CMD_T withResultCallback(ResultCallback<A_RES_T> resultCallback) {
        checkNotNull(resultCallback, "resultCallback was not specified");
        this.resultCallback = resultCallback;
        return ((CMD_T) this);
    }

    @Override
    public ResultCallback<A_RES_T> getResultCallback() {
        return resultCallback;
    }

}
