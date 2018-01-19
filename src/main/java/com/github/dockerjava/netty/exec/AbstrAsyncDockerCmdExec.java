package com.github.dockerjava.netty.exec;

import com.github.dockerjava.api.command.AsyncDockerCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.WebTarget;

public abstract class AbstrAsyncDockerCmdExec<CMD_T extends AsyncDockerCmd<CMD_T, A_RES_T>, A_RES_T> extends
        com.github.dockerjava.core.exec.AbstrAsyncDockerCmdExec<CMD_T, A_RES_T> {

    public AbstrAsyncDockerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }
}