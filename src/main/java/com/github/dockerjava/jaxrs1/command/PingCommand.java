package com.github.dockerjava.jaxrs1.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.PingCmd;
import com.github.dockerjava.client.command.AbstrDockerCmd;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Ping the Docker server
 * 
 */
public class PingCommand extends AbstrDockerCmd<PingCommand, Void> implements PingCmd {

	private static final Logger LOGGER = LoggerFactory.getLogger(PingCommand.class);

	protected Void impl() {
		WebResource webResource = baseResource.path("/_ping");

		LOGGER.trace("GET: {}", webResource);
		webResource.get(ClientResponse.class);
		return null;
	}
}
