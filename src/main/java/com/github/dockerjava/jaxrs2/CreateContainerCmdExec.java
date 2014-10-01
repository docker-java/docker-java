package com.github.dockerjava.jaxrs2;

import static javax.ws.rs.client.Entity.entity;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;

public class CreateContainerCmdExec extends AbstrDockerCmdExec<CreateContainerCmd, CreateContainerResponse> implements CreateContainerCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CreateContainerCmdExec.class);
	
	public CreateContainerCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected CreateContainerResponse execute(CreateContainerCmd command) {
		WebTarget webResource = getBaseResource().path("/containers/create");

        if (command.getName() != null) {
            webResource = webResource.queryParam("name", command.getName());
        }

		LOGGER.trace("POST: {} ", webResource);
		return webResource.request().accept(MediaType.APPLICATION_JSON)
				.post(entity(command, MediaType.APPLICATION_JSON), CreateContainerResponse.class);
	}

}
