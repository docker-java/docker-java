package com.github.dockerjava.netty.exec;


import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.WebTarget;

/**
 * Update swarmNode spec
 */
public class UpdateSwarmNodeCmdExec extends com.github.dockerjava.core.exec.UpdateSwarmNodeCmdExec {

    public UpdateSwarmNodeCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }
}
