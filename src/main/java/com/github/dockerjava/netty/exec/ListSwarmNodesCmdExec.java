package com.github.dockerjava.netty.exec;

import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.WebTarget;

public class ListSwarmNodesCmdExec extends com.github.dockerjava.core.exec.ListSwarmNodesCmdExec {

    public ListSwarmNodesCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }
}
