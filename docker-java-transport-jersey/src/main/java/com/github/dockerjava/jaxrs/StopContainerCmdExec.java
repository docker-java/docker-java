package com.github.dockerjava.jaxrs;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.StopContainerCmd;
import com.github.dockerjava.core.DockerClientConfig;

public class StopContainerCmdExec extends AbstrSyncDockerCmdExec<StopContainerCmd, Void> implements
        StopContainerCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(StopContainerCmdExec.class);

    public StopContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(StopContainerCmd command) {
        WebTarget webResource = getBaseResource().path("/containers/{id}/stop").resolveTemplate("id",
                command.getContainerId());

        if (command.getTimeout() != null) {
            webResource = webResource.queryParam("t", String.valueOf(command.getTimeout()));
        }

        LOGGER.trace("POST: {}", webResource);
        webResource.request().accept(MediaType.APPLICATION_JSON).post(null).close();

        return null;
    }
}
