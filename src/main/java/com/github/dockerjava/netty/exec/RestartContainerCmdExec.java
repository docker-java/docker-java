package com.github.dockerjava.netty.exec;

import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.WebTarget;

public class RestartContainerCmdExec extends com.github.dockerjava.core.exec.RestartContainerCmdExec {

    public RestartContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }
}
