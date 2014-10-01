package com.github.dockerjava.jaxrs1;

import java.io.InputStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.PushImageCmd;
import com.github.dockerjava.api.model.AuthConfig;
import com.sun.jersey.api.client.WebResource;

public class PushImageCmdExec extends AbstrDockerCmdExec<PushImageCmd, InputStream> implements PushImageCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PushImageCmdExec.class);
	
	public PushImageCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected InputStream execute(PushImageCmd command) {
		WebResource webResource = getBaseResource()
		        .path("/images/" + name(command) + "/push")
		        .build();

		final String registryAuth = registryAuth(command.getAuthConfig());
		LOGGER.trace("POST: {}", webResource);
		return webResource
				.header("X-Registry-Auth", registryAuth)
				.accept(MediaType.APPLICATION_JSON)
				.entity(Response.class, MediaType.APPLICATION_JSON)
				.post(InputStream.class);
	}
	
	private String name(PushImageCmd command) {
		String name = command.getName();
		AuthConfig authConfig = command.getAuthConfig();
		return name.contains("/") ? name : authConfig.getUsername();
	}

}
