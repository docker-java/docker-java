package com.github.dockerjava.client.command;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.model.AuthConfig;
import javax.ws.rs.client.WebTarget;

import static javax.ws.rs.client.Entity.entity;

/**
 * 
 *  Authenticate with the server, useful for checking authentication.
 * 
 */
public class AuthCmd extends AbstrAuthCfgDockerCmd<AuthCmd, Void> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthCmd.class);

	public AuthCmd(AuthConfig authConfig) {
		withAuthConfig(authConfig);
	}
	
	protected Void impl() throws DockerException {
		try {
			WebTarget webResource = baseResource.path("/auth");
			LOGGER.trace("POST: {}", webResource);
			webResource.request().accept(MediaType.APPLICATION_JSON).post(entity(authConfig, MediaType.APPLICATION_JSON));
			return null;
		} catch (ClientErrorException e) {
			throw new DockerException(e);
		}
	}
	
	@Override
	public String toString() {
		return "authenticate using " + this.authConfig;
	}
}
