package com.github.dockerjava.netty.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.RestartContainerCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.MediaType;
import com.github.dockerjava.netty.WebTarget;

public class RestartContainerCmdExec extends AbstrSyncDockerCmdExec<RestartContainerCmd, Void> implements
        RestartContainerCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestartContainerCmdExec.class);

    public RestartContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(RestartContainerCmd command) {
        WebTarget webResource = getBaseResource().path("/containers/{id}/restart").resolveTemplate("id",
                command.getContainerId());

        if (command.getTimeout() != null) {
            webResource = webResource.queryParam("t", String.valueOf(command.getTimeout()));
        }

        LOGGER.trace("POST: {}", webResource);
        webResource.request().accept(MediaType.APPLICATION_JSON).post(null);

        return null;
    }

}
