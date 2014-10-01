package com.github.dockerjava.jaxrs1;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.sun.jersey.api.client.WebResource;

public class CreateContainerCmdExec extends AbstrDockerCmdExec<CreateContainerCmd, CreateContainerResponse> implements CreateContainerCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CreateContainerCmdExec.class);
	
	public CreateContainerCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected CreateContainerResponse execute(CreateContainerCmd command) {
		WebResource webResource = getBaseResource().path("/containers/create").build();

        if (command.getName() != null) {
            webResource = webResource.queryParam("name", command.getName());
        }

		LOGGER.trace("POST: {} ", webResource);
		return webResource.accept(MediaType.APPLICATION_JSON)
				.entity(command, MediaType.APPLICATION_JSON)
				.post(CreateContainerResponse.class);
	}

}
