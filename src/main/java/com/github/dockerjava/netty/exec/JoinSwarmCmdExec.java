package com.github.dockerjava.netty.exec;


import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.WebTarget;

public class JoinSwarmCmdExec extends com.github.dockerjava.core.exec.JoinSwarmCmdExec {

    public JoinSwarmCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }
}