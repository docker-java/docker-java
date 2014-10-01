package com.github.dockerjava.jaxrs1;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.TagImageCmd;
import com.sun.jersey.api.client.WebResource;

public class TagImageCmdExec extends AbstrDockerCmdExec<TagImageCmd, Void> implements TagImageCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TagImageCmdExec.class);
	
	public TagImageCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected Void execute(TagImageCmd command) {
		WebResource webResource = getBaseResource()
		        .path("/images/" + command.getImageId() + "/tag")
                .queryParam("repo", command.getRepository())
                .queryParam("tag", command.getTag())
                .queryParam("force", command.hasForceEnabled() ? "1" : "0")
                .build();

		LOGGER.trace("POST: {}", webResource);
		webResource.entity(null, MediaType.APPLICATION_JSON)
		    .post();;
		return null;
	}
	
	

}
