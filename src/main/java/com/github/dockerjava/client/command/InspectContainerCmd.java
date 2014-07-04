package com.github.dockerjava.client.command;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.NotFoundException;
import com.github.dockerjava.client.model.ContainerInspectResponse;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

/**
 * Inspect the details of a container.
 */
public class InspectContainerCmd extends AbstrDockerCmd<InspectContainerCmd, ContainerInspectResponse> {

	private static final Logger LOGGER = LoggerFactory.getLogger(InspectContainerCmd.class);

	private String containerId;
	
	public InspectContainerCmd(String containerId) {
		withContainerId(containerId);
	}
	
	public InspectContainerCmd withContainerId(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}
	
    @Override
    public String toString() {
        return "inspect " + containerId;
    }

	protected ContainerInspectResponse impl() throws DockerException {
		WebResource webResource = baseResource.path(String.format("/containers/%s/json", containerId));

		try {
			LOGGER.trace("GET: {}", webResource);
			return webResource.accept(MediaType.APPLICATION_JSON).get(ContainerInspectResponse.class);
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 404) {
				throw new NotFoundException(String.format("No such container %s", containerId));
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}
}
