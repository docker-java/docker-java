package com.github.dockerjava.jaxrs1;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.sun.jersey.api.client.WebResource;

public class InspectContainerCmdExec extends AbstrDockerCmdExec<InspectContainerCmd, InspectContainerResponse> implements InspectContainerCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(InspectContainerCmdExec.class);
	
	public InspectContainerCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected InspectContainerResponse execute(InspectContainerCmd command) {
		WebResource webResource = getBaseResource()
		        .resolveTemplate("/containers/{id}/json", command.getContainerId())
		        .build();
		
		LOGGER.debug("GET: {}", webResource);
		return webResource.accept(MediaType.APPLICATION_JSON).get(InspectContainerResponse.class);
	}

}
