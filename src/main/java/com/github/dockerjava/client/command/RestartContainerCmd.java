package com.github.dockerjava.client.command;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.NotFoundException;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

/**
 * Restart a running container.
 * 
 * @param timeout - Timeout in seconds before killing the container. Defaults to 10 seconds.
 * 
 */
public class RestartContainerCmd extends AbstrDockerCmd<RestartContainerCmd, Void> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestartContainerCmd.class);

	private String containerId;
	
	private int timeout = 10;
	
	public RestartContainerCmd(String containerId) {
		withContainerId(containerId);
	}
	
	public RestartContainerCmd withContainerId(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}
	
	public RestartContainerCmd withtTimeout(int timeout) {
		Preconditions.checkArgument(timeout >= 0, "timeout must be greater or equal 0");
		this.timeout = timeout;
		return this;
	}
	
	protected Void impl() throws DockerException {
		WebResource webResource = baseResource.path(String.format("/containers/%s/restart", containerId))
				.queryParam("t", String.valueOf(timeout));;

		try {
			LOGGER.trace("POST: {}", webResource);
			webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post();
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 404) {
				throw new NotFoundException(String.format("No such container %s", containerId));
			} else if (exception.getResponse().getStatus() == 204) {
				//no error
				LOGGER.trace("Successfully restarted container {}", containerId);
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}
		
		return null;
	}
}
