package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.ResizeContainerCmd;
import com.github.dockerjava.core.DockerClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class ResizeContainerCmdExec extends AbstrSyncDockerCmdExec<ResizeContainerCmd, Void> implements ResizeContainerCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResizeContainerCmdExec.class);

    public ResizeContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(ResizeContainerCmd command) {
        WebTarget webResource = getBaseResource().path("/containers/{id}/resize")
                .resolveTemplate("id", command.getContainerId()).queryParam("h", command.getHeight())
                .queryParam("w", command.getWidth());

        LOGGER.trace("POST: {}", webResource);

        webResource.request().accept(MediaType.APPLICATION_JSON).post(null).close();

        return null;
    }
}
