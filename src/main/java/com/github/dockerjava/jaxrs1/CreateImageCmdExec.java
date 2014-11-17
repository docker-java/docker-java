package com.github.dockerjava.jaxrs1;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.CreateImageCmd;
import com.github.dockerjava.api.command.CreateImageResponse;
import com.sun.jersey.api.client.WebResource;

public class CreateImageCmdExec extends AbstrDockerCmdExec<CreateImageCmd, CreateImageResponse> implements CreateImageCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CreateImageCmdExec.class);
	
	public CreateImageCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected CreateImageResponse execute(CreateImageCmd command) {
		WebResource webResource = getBaseResource()
                .path("/images/create")
                .queryParam("repo", command.getRepository())
                .queryParam("tag", command.getTag())
                .queryParam("fromSrc", "-")
                .build();
		
		LOGGER.trace("POST: {}", webResource);
		return webResource.accept(MediaType.APPLICATION_OCTET_STREAM_TYPE)
				.entity(command.getImageStream(), MediaType.APPLICATION_OCTET_STREAM)
				.post(CreateImageResponse.class);
	}
}
