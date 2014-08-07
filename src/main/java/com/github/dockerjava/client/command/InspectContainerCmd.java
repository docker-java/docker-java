package com.github.dockerjava.client.command;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.NotFoundException;
import com.google.common.base.Preconditions;
import javax.ws.rs.client.WebTarget;

/**
 * Inspect the details of a container.
 */
public class InspectContainerCmd extends AbstrDockerCmd<InspectContainerCmd, InspectContainerResponse> {

	private static final Logger LOGGER = LoggerFactory.getLogger(InspectContainerCmd.class);

	private String containerId;

	public InspectContainerCmd(String containerId) {
		withContainerId(containerId);
	}

    public String getContainerId() {
        return containerId;
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

	protected InspectContainerResponse impl() throws DockerException {
		WebTarget webResource = baseResource.path("/containers/{id}/json").resolveTemplate("id", containerId);

		try {
			LOGGER.trace("GET: {}", webResource);
			return webResource.request().accept(MediaType.APPLICATION_JSON).get(InspectContainerResponse.class);
		} catch (ClientErrorException exception) {
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
