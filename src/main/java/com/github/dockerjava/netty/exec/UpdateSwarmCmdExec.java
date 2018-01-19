package com.github.dockerjava.netty.exec;


import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.WebTarget;

public class UpdateSwarmCmdExec extends com.github.dockerjava.core.exec.UpdateSwarmCmdExec {

    public UpdateSwarmCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }
}