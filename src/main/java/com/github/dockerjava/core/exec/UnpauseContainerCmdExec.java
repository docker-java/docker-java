package com.github.dockerjava.core.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.UnpauseContainerCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;

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
        webResource.request().accept(MediaType.APPLICATION_JSON).post(null);

        return null;
    }

}
