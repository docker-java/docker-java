package com.github.dockerjava.jaxrs;

import static javax.ws.rs.client.Entity.entity;

import java.io.InputStream;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.PullImageCmd;

public class PullImageCmdExec extends AbstrDockerCmdExec<PullImageCmd, InputStream> implements PullImageCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PullImageCmdExec.class);
	
	public PullImageCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected InputStream execute(PullImageCmd command) {
		WebTarget webResource = getBaseResource().path("/images/create")
                .queryParam("tag", command.getTag())
                .queryParam("fromImage", command.getRepository())
                .queryParam("registry", command.getRegistry());
		
		LOGGER.trace("POST: {}", webResource);
		return webResource.request()
				.accept(MediaType.APPLICATION_OCTET_STREAM_TYPE)
				.post(entity(Response.class, MediaType.APPLICATION_JSON)).readEntity(InputStream.class);
	}

}
