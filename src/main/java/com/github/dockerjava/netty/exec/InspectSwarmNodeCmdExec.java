package com.github.dockerjava.netty.exec;


import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.WebTarget;

public class InspectSwarmNodeCmdExec extends com.github.dockerjava.core.exec.InspectSwarmNodeCmdExec {

    public InspectSwarmNodeCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }
}
