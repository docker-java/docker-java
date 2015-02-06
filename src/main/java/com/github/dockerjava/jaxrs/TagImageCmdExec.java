package com.github.dockerjava.jaxrs;

import javax.ws.rs.client.WebTarget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.TagImageCmd;

public class TagImageCmdExec extends AbstrDockerCmdExec<TagImageCmd, Void> implements TagImageCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TagImageCmdExec.class);
	
	public TagImageCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected Void execute(TagImageCmd command) {
		WebTarget webResource = getBaseResource().path("/images/" + command.getImageId() + "/tag")
                .queryParam("repo", command.getRepository())
                .queryParam("tag", command.getTag())
                .queryParam("force", command.hasForceEnabled() ? "1" : "0");

		LOGGER.trace("POST: {}", webResource);
		webResource.request().post(null).close();
		return null;
	}
	
	

}
