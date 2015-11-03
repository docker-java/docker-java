package com.github.dockerjava.jaxrs.jersey.jaxrs;

import com.github.dockerjava.api.DockerClientConfig;
import com.github.dockerjava.api.command.ExecCreateCmd;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class ExecCreateCmdExec extends AbstrSyncDockerCmdExec<ExecCreateCmd, ExecCreateCmdResponse> implements
        ExecCreateCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(VersionCmdExec.class);

    public ExecCreateCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected ExecCreateCmdResponse execute(ExecCreateCmd command) {
        WebTarget webResource = getBaseResource().path("/containers/{id}/exec").resolveTemplate("id",
                command.getContainerId());

        LOGGER.trace("POST: {}", webResource);

        return webResource.request().accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(command, MediaType.APPLICATION_JSON), ExecCreateCmdResponse.class);
    }
}
