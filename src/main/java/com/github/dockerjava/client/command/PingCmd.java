package com.github.dockerjava.client.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

/**
 * Ping the Docker server
 * 
 */
public class PingCmd extends AbstrDockerCmd<PingCmd, Void> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PingCmd.class);

	/**
	 * A {@link DockerException} is thrown if something gets wrong
	 */
	@Override
	public Void exec() {
		return super.exec();
	}

	protected Void impl() {
		WebResource webResource = baseResource.path("/_ping");

		LOGGER.trace("GET: {}", webResource);
		webResource.get(ClientResponse.class);
		return null;
	}
}
