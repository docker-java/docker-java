package com.github.dockerjava.netty.exec;

import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.WebTarget;

/**
 * Update service settings.
 */
public class UpdateServiceCmdExec extends com.github.dockerjava.core.exec.UpdateServiceCmdExec {

    public UpdateServiceCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }
}