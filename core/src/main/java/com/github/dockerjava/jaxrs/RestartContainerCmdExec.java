package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.DockerClientConfig;
import com.github.dockerjava.api.command.RestartContainerCmd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class RestartContainerCmdExec extends AbstrSyncDockerCmdExec<RestartContainerCmd, Void> implements
        RestartContainerCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestartContainerCmdExec.class);

    public RestartContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(RestartContainerCmd command) {
        WebTarget webResource = getBaseResource().path("/containers/{id}/restart")
                .resolveTemplate("id", command.getContainerId()).queryParam("t", String.valueOf(command.getTimeout()));

        LOGGER.trace("POST: {}", webResource);
        webResource.request().accept(MediaType.APPLICATION_JSON).post(null).close();

        return null;
    }

}
