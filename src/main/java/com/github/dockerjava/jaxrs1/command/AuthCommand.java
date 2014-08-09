package com.github.dockerjava.jaxrs1.command;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.command.AuthCmd;
import com.github.dockerjava.client.command.AbstrAuthCfgDockerCmd;
import com.github.dockerjava.client.model.AuthConfig;
import com.sun.jersey.api.client.WebResource;

/**
 * 
 * Authenticate with the server, useful for checking authentication.
 * 
 */
public class AuthCommand extends AbstrAuthCfgDockerCmd<AuthCommand, Void> implements AuthCmd {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthCommand.class);

	public AuthCommand(AuthConfig authConfig) {
		withAuthConfig(authConfig);
	}
	
	protected Void impl() throws DockerException {

		WebResource webResource = baseResource.path("/auth");
		LOGGER.trace("POST: {}", webResource);
		webResource.header("Content-Type", MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).post(authConfig);
		return null;
	}

	@Override
	public String toString() {
		return "authenticate using " + this.authConfig;
	}
}
