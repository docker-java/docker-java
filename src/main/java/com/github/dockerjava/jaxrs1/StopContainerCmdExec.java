package com.github.dockerjava.jaxrs1;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.StopContainerCmd;
import com.sun.jersey.api.client.WebResource;

public class StopContainerCmdExec extends AbstrDockerCmdExec<StopContainerCmd, Void> implements StopContainerCmd.Exec {

	private static final Logger LOGGER = LoggerFactory.getLogger(StopContainerCmdExec.class);

	public StopContainerCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected Void execute(StopContainerCmd command) {
		WebResource webResource = getBaseResource()
		        .resolveTemplate("/containers/{id}/stop", command.getContainerId())
				.queryParam("t", String.valueOf(command.getTimeout()))
				.build();
		
		LOGGER.trace("POST: {}", webResource);
		webResource.accept(MediaType.APPLICATION_JSON)
		    .entity(null, MediaType.APPLICATION_JSON)
		    .post();

		return null;
	}
}
