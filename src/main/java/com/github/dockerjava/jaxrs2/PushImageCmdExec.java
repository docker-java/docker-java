package com.github.dockerjava.jaxrs2;

import static javax.ws.rs.client.Entity.entity;

import java.io.InputStream;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.PushImageCmd;
import com.github.dockerjava.api.model.AuthConfig;

public class PushImageCmdExec extends AbstrDockerCmdExec<PushImageCmd, InputStream> implements PushImageCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PushImageCmdExec.class);
	
	public PushImageCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected InputStream execute(PushImageCmd command) {
		WebTarget webResource = getBaseResource().path("/images/" + name(command) + "/push");

		final String registryAuth = registryAuth(command.getAuthConfig());
		LOGGER.trace("POST: {}", webResource);
		return webResource
                .request()
				.header("X-Registry-Auth", registryAuth)
				.accept(MediaType.APPLICATION_JSON)
				.post(entity(Response.class, MediaType.APPLICATION_JSON)).readEntity(InputStream.class);
	}
	
	private String name(PushImageCmd command) {
		String name = command.getName();
		AuthConfig authConfig = command.getAuthConfig();
		return name.contains("/") ? name : authConfig.getUsername();
	}

}
