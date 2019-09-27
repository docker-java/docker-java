package com.github.dockerjava.jaxrs;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.PauseContainerCmd;
import com.github.dockerjava.core.DockerClientConfig;

public class PauseContainerCmdExec extends AbstrSyncDockerCmdExec<PauseContainerCmd, Void> implements
        PauseContainerCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(PauseContainerCmdExec.class);

    public PauseContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(PauseContainerCmd command) {
        WebTarget webResource = getBaseResource().path("/containers/{id}/pause").resolveTemplate("id",
                command.getContainerId());

        LOGGER.trace("POST: {}", webResource);
        webResource.request().accept(MediaType.APPLICATION_JSON).post(null).close();

        return null;
    }

}
