package com.github.dockerjava.jaxrs;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.core.DockerClientConfig;

public class StartContainerCmdExec extends AbstrSyncDockerCmdExec<StartContainerCmd, Void> implements
        StartContainerCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartContainerCmdExec.class);

    public StartContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(StartContainerCmd command) {
        WebTarget webResource = getBaseResource().path("/containers/{id}/start")
                .resolveTemplate("id", command.getContainerId());

        LOGGER.trace("POST: {}", webResource);
        webResource.request()
                .accept(MediaType.APPLICATION_JSON)
                .post(null)
                .close();

        return null;
    }

}
