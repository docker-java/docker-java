package com.github.dockerjava.jaxrs1;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.InspectImageCmd;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.sun.jersey.api.client.WebResource;

public class InspectImageCmdExec extends AbstrDockerCmdExec<InspectImageCmd, InspectImageResponse> implements InspectImageCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(InspectImageCmdExec.class);
	
	public InspectImageCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected InspectImageResponse execute(InspectImageCmd command) {
		WebResource webResource = getBaseResource()
		        .resolveTemplate("/images/{id}/json", command.getImageId())
		        .build();

		LOGGER.trace("GET: {}", webResource);
		return webResource.accept(MediaType.APPLICATION_JSON).get(InspectImageResponse.class);
	}

}
