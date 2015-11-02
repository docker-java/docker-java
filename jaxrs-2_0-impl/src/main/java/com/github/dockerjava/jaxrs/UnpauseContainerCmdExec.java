package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.DockerClientConfig;
import com.github.dockerjava.api.command.UnpauseContainerCmd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class UnpauseContainerCmdExec extends AbstrSyncDockerCmdExec<UnpauseContainerCmd, Void> implements
        UnpauseContainerCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnpauseContainerCmdExec.class);

    public UnpauseContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(UnpauseContainerCmd command) {
        WebTarget webResource = getBaseResource().path("/containers/{id}/unpause").resolveTemplate("id",
                command.getContainerId());

        LOGGER.trace("POST: {}", webResource);
        webResource.request().accept(MediaType.APPLICATION_JSON).post(null).close();

        return null;
    }

}
