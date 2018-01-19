package com.github.dockerjava.netty.exec;

import com.github.dockerjava.api.command.DockerCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.WebTarget;

public abstract class AbstrSyncDockerCmdExec<CMD_T extends DockerCmd<RES_T>, RES_T>
        extends com.github.dockerjava.core.exec.AbstrSyncDockerCmdExec<CMD_T, RES_T> {

    public AbstrSyncDockerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }
}
