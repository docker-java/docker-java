package com.github.dockerjava.jaxrs;

import static javax.ws.rs.client.Entity.entity;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.CreateExecCmd;
import com.github.dockerjava.api.command.CreateExecCmdResponse;
import com.github.dockerjava.core.DockerClientConfig;

public class CreateExecCmdExec extends AbstrSyncDockerCmdExec<CreateExecCmd, CreateExecCmdResponse> implements
        CreateExecCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(VersionCmdExec.class);

    public CreateExecCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected CreateExecCmdResponse execute(CreateExecCmd command) {
        WebTarget webResource = getBaseResource().path("/containers/{id}/exec").resolveTemplate("id",
                command.getContainerId());

        LOGGER.trace("POST: {}", webResource);

        return webResource.request().accept(MediaType.APPLICATION_JSON)
                .post(entity(command, MediaType.APPLICATION_JSON), CreateExecCmdResponse.class);
    }
}
