package com.github.dockerjava.jaxrs2;

import static javax.ws.rs.client.Entity.entity;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.PauseContainerCmd;

public class PauseContainerCmdExec extends AbstrDockerCmdExec<PauseContainerCmd, Void> implements PauseContainerCmd.Exec {

	private static final Logger LOGGER = LoggerFactory.getLogger(PauseContainerCmdExec.class);

	public PauseContainerCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected Void execute(PauseContainerCmd command) {
		WebTarget webResource = getBaseResource().path("/containers/{id}/pause")
				.resolveTemplate("id", command.getContainerId());
		
		LOGGER.trace("POST: {}", webResource);
		webResource.request()
				.accept(MediaType.APPLICATION_JSON)
				.post(entity(null, MediaType.APPLICATION_JSON), Response.class);

		return null;
	}

}
