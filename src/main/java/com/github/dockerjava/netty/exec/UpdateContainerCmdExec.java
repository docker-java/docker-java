package com.github.dockerjava.netty.exec;

import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.WebTarget;


/**
 * @author Kanstantsin Shautsou
 */
public class UpdateContainerCmdExec extends com.github.dockerjava.core.exec.UpdateContainerCmdExec {

    public UpdateContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }
}