package com.github.dockerjava.jaxrs1;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.PauseContainerCmd;
import com.sun.jersey.api.client.WebResource;

public class PauseContainerCmdExec extends AbstrDockerCmdExec<PauseContainerCmd, Void> implements PauseContainerCmd.Exec {

	private static final Logger LOGGER = LoggerFactory.getLogger(PauseContainerCmdExec.class);

	public PauseContainerCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected Void execute(PauseContainerCmd command) {
		WebResource webResource = getBaseResource()
		        .resolveTemplate("/containers/{id}/pause", command.getContainerId())
		        .build();
		
		LOGGER.trace("POST: {}", webResource);
		webResource
				.accept(MediaType.APPLICATION_JSON)
				.entity(null, MediaType.APPLICATION_JSON)
				.post(Response.class);

		return null;
	}

}
