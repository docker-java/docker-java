package com.github.dockerjava.netty.exec;

import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.WebTarget;

public class SaveImageCmdExec extends com.github.dockerjava.core.exec.SaveImageCmdExec {

    public SaveImageCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }
}
