package com.github.dockerjava.netty.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.CreateExecCmd;
import com.github.dockerjava.api.command.CreateExecCmdResponse;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.MediaType;
import com.github.dockerjava.netty.WebTarget;

public class CreateExecCmdExec extends AbstrSyncDockerCmdExec<CreateExecCmd, CreateExecCmdResponse> implements
        CreateExecCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateExecCmdExec.class);

    public CreateExecCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected CreateExecCmdResponse execute(CreateExecCmd command) {
        WebTarget webResource = getBaseResource().path("/containers/{id}/exec").resolveTemplate("id",
                command.getContainerId());

        LOGGER.trace("POST: {}", webResource);

        return webResource.request().accept(MediaType.APPLICATION_JSON)
                .post(command, new TypeReference<CreateExecCmdResponse>() {
                });
    }
}
