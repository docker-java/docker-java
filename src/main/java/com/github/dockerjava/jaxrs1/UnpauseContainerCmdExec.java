package com.github.dockerjava.jaxrs1;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.UnpauseContainerCmd;
import com.sun.jersey.api.client.WebResource;

public class UnpauseContainerCmdExec extends AbstrDockerCmdExec<UnpauseContainerCmd, Void> implements UnpauseContainerCmd.Exec {

	private static final Logger LOGGER = LoggerFactory.getLogger(UnpauseContainerCmdExec.class);

	public UnpauseContainerCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected Void execute(UnpauseContainerCmd command) {
		WebResource webResource = getBaseResource()
		        .resolveTemplate("/containers/{id}/unpause", command.getContainerId())
		        .build();
		
		LOGGER.trace("POST: {}", webResource);
		webResource.accept(MediaType.APPLICATION_JSON)
				.entity(Response.class, MediaType.APPLICATION_JSON)
				.post();

		return null;
	}

}
