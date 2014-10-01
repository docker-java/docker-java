package com.github.dockerjava.jaxrs1;

import java.io.InputStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.PullImageCmd;
import com.sun.jersey.api.client.WebResource;

public class PullImageCmdExec extends AbstrDockerCmdExec<PullImageCmd, InputStream> implements PullImageCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PullImageCmdExec.class);
	
	public PullImageCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected InputStream execute(PullImageCmd command) {
		WebResource webResource = getBaseResource().path("/images/create")
                .queryParam("tag", command.getTag())
                .queryParam("fromImage", command.getRepository())
                .queryParam("registry", command.getRegistry())
                .build();
		
		LOGGER.trace("POST: {}", webResource);
		return webResource
				.accept(MediaType.APPLICATION_OCTET_STREAM_TYPE)
				.post(InputStream.class);
	}

}
