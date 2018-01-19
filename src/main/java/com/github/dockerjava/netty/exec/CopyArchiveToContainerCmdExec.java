package com.github.dockerjava.netty.exec;

import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.WebTarget;

public class CopyArchiveToContainerCmdExec extends com.github.dockerjava.core.exec.CopyArchiveToContainerCmdExec {

    public CopyArchiveToContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }
}