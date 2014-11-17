package com.github.dockerjava.jaxrs2;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.InspectImageCmd;
import com.github.dockerjava.api.command.InspectImageResponse;

public class InspectImageCmdExec extends AbstrDockerCmdExec<InspectImageCmd, InspectImageResponse> implements InspectImageCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(InspectImageCmdExec.class);
	
	public InspectImageCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected InspectImageResponse execute(InspectImageCmd command) {
		WebTarget webResource = getBaseResource().path("/images/{id}/json").resolveTemplate("id", command.getImageId());

		LOGGER.trace("GET: {}", webResource);
		return webResource.request().accept(MediaType.APPLICATION_JSON).get(InspectImageResponse.class);
	}

}
