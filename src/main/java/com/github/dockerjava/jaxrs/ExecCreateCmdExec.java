package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.ExecCreateCmd;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import static javax.ws.rs.client.Entity.entity;

public class ExecCreateCmdExec extends AbstrDockerCmdExec<ExecCreateCmd, ExecCreateCmdResponse> implements ExecCreateCmd.Exec {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(VersionCmdExec.class);

    public ExecCreateCmdExec(WebTarget baseResource) {
        super(baseResource);
    }

    @Override
    protected ExecCreateCmdResponse execute(ExecCreateCmd command) {
        WebTarget webResource = getBaseResource().path("/containers/{id}/exec").resolveTemplate("id", command.getContainerId());

        LOGGER.trace("POST: {}", webResource);

		return webResource
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.post(entity(command, MediaType.APPLICATION_JSON), ExecCreateCmdResponse.class);
    }
}
