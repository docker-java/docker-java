package com.github.dockerjava.jaxrs1;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.RestartContainerCmd;
import com.sun.jersey.api.client.WebResource;

public class RestartContainerCmdExec extends AbstrDockerCmdExec<RestartContainerCmd, Void> implements RestartContainerCmd.Exec {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestartContainerCmdExec.class);

	public RestartContainerCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected Void execute(RestartContainerCmd command) {
		WebResource webResource = getBaseResource()
		        .resolveTemplate("/containers/{id}/restart", command.getContainerId())
				.queryParam("t", String.valueOf(command.getTimeout()))
				.build();
		
		LOGGER.trace("POST: {}", webResource);
		webResource.accept(MediaType.APPLICATION_JSON)
		    .entity(null, MediaType.APPLICATION_JSON_TYPE)
		    .post();

		return null;
	}

}
