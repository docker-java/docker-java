package com.github.dockerjava.jaxrs2;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.UnpauseContainerCmd;

public class UnpauseContainerCmdExec extends AbstrDockerCmdExec<UnpauseContainerCmd, Void> implements UnpauseContainerCmd.Exec {

	private static final Logger LOGGER = LoggerFactory.getLogger(UnpauseContainerCmdExec.class);

	public UnpauseContainerCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected Void execute(UnpauseContainerCmd command) {
		WebTarget webResource = getBaseResource().path("/containers/{id}/unpause")
				.resolveTemplate("id", command.getContainerId());
		
		LOGGER.trace("POST: {}", webResource);
		webResource.request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(Response.class, MediaType.APPLICATION_JSON));

		return null;
	}

}
