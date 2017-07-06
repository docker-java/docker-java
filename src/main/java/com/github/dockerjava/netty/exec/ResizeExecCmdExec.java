package com.github.dockerjava.netty.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.ResizeExecCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.MediaType;
import com.github.dockerjava.netty.WebTarget;

public class ResizeExecCmdExec extends AbstrSyncDockerCmdExec<ResizeExecCmd, Void> implements ResizeExecCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResizeExecCmdExec.class);

    public ResizeExecCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(ResizeExecCmd command) {
        WebTarget webResource = getBaseResource().path("/exec/{id}/resize").resolveTemplate("id", command.getExecId())
                .queryParam("h", command.getHeight()).queryParam("w", command.getWidth());

        LOGGER.trace("POST: {}", webResource);
        webResource.request().accept(MediaType.APPLICATION_JSON).post(command);

        return null;
    }
}
