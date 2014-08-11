package com.github.dockerjava.client.command;

import static javax.ws.rs.client.Entity.entity;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.UnauthorizedException;
import com.github.dockerjava.api.command.AuthCmd;
import com.github.dockerjava.api.model.AuthConfig;

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
	
	@Override
	public Void exec() throws UnauthorizedException {
		return super.exec();
	}
	
	protected Void impl() throws UnauthorizedException {
		WebTarget webResource = baseResource.path("/auth");
		LOGGER.trace("POST: {}", webResource);
		Response response = webResource.request().accept(MediaType.APPLICATION_JSON).post(entity(authConfig, MediaType.APPLICATION_JSON));
		
		if(response.getStatus() == 401) {
			throw new UnauthorizedException("Unauthorized");
		};
		
		return null;
	}
	
	@Override
	public String toString() {
		return "authenticate using " + this.authConfig;
	}
}
