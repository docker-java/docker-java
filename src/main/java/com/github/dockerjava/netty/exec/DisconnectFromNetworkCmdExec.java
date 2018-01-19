package com.github.dockerjava.netty.exec;

import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.WebTarget;

public class DisconnectFromNetworkCmdExec extends com.github.dockerjava.core.exec.DisconnectFromNetworkCmdExec {

    public DisconnectFromNetworkCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }
}