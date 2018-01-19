package com.github.dockerjava.netty.exec;

import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.WebTarget;

public class RemoveSwarmNodeCmdExec extends com.github.dockerjava.core.exec.RemoveSwarmNodeCmdExec {

    public RemoveSwarmNodeCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }
}