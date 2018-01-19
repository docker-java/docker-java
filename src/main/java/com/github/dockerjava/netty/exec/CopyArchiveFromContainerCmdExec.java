package com.github.dockerjava.netty.exec;

import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.WebTarget;

public class CopyArchiveFromContainerCmdExec extends com.github.dockerjava.core.exec.CopyArchiveFromContainerCmdExec {

    public CopyArchiveFromContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }
}