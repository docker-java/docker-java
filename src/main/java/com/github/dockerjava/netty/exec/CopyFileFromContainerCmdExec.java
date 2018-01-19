package com.github.dockerjava.netty.exec;

import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.WebTarget;

public class CopyFileFromContainerCmdExec extends com.github.dockerjava.core.exec.CopyFileFromContainerCmdExec {

    public CopyFileFromContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }
}
