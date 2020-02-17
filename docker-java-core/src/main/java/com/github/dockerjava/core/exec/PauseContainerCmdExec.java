package com.github.dockerjava.core.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.PauseContainerCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;

import java.io.IOException;

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
        try {
            webResource.request().accept(MediaType.APPLICATION_JSON).post(null).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

}
