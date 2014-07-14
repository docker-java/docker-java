package com.github.dockerjava.client.command;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

/**
 * Unpause a container.
 * 
 * @param containerId - Id of the container
 * 
 */
public class UnpauseContainerCmd extends AbstrDockerCmd<UnpauseContainerCmd, Integer> {

	private static final Logger LOGGER = LoggerFactory.getLogger(UnpauseContainerCmd.class);

	private String containerId;
	
	public UnpauseContainerCmd(String containerId) {
		withContainerId(containerId);
	}
	
	public UnpauseContainerCmd withContainerId(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}
	
	@Override
    public String toString() {
        return new StringBuilder("pause ")
            .append(containerId)
            .toString();
    }   

	protected Integer impl() throws DockerException {
		WebResource webResource = baseResource.path(String.format("/containers/%s/unpause", containerId));

		ClientResponse response = null;
		
		try {
			LOGGER.trace("POST: {}", webResource);
			response = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(ClientResponse.class);
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 404) {
				LOGGER.warn("No such container {}", containerId);
			} else if (exception.getResponse().getStatus() == 204) {
				//no error
				LOGGER.trace("Successfully paused container {}", containerId);
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}
		
		return response.getStatus();
	}
}
