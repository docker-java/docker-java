package com.github.dockerjava.jaxrs2;

import static javax.ws.rs.client.Entity.entity;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.CreateImageCmd;
import com.github.dockerjava.api.command.CreateImageResponse;

public class CreateImageCmdExec extends AbstrDockerCmdExec<CreateImageCmd, CreateImageResponse> implements CreateImageCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CreateImageCmdExec.class);
	
	public CreateImageCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected CreateImageResponse execute(CreateImageCmd command) {
		WebTarget webResource = getBaseResource()
                .path("/images/create")
                .queryParam("repo", command.getRepository())
                .queryParam("tag", command.getTag())
                .queryParam("fromSrc", "-");
		
		LOGGER.trace("POST: {}", webResource);
		return webResource.request().accept(MediaType.APPLICATION_OCTET_STREAM_TYPE)
				.post(entity(command.getImageStream(), MediaType.APPLICATION_OCTET_STREAM), CreateImageResponse.class);
	}
}
