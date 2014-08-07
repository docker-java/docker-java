package com.github.dockerjava.client.command;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.NotFoundException;
import com.google.common.base.Preconditions;
import javax.ws.rs.client.WebTarget;

import static javax.ws.rs.client.Entity.entity;

/**
 * Wait for a container to exit and print its exit code
 */
public class WaitContainerCmd extends AbstrDockerCmd<WaitContainerCmd, Integer> {

	private static final Logger LOGGER = LoggerFactory.getLogger(WaitContainerCmd.class);

	private String containerId;

	public WaitContainerCmd(String containerId) {
		withContainerId(containerId);
	}

    public String getContainerId() {
        return containerId;
    }

    public WaitContainerCmd withContainerId(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}

    @Override
    public String toString() {
        return "wait " + containerId;
    }

	protected Integer impl() throws DockerException {
		WebTarget webResource = baseResource.path("/containers/{id}/wait").resolveTemplate("id", containerId);

		try {
			LOGGER.trace("POST: {}", webResource);
			ObjectNode ObjectNode = webResource.request().accept(MediaType.APPLICATION_JSON).post(entity(null, MediaType.APPLICATION_JSON), ObjectNode.class);
            return ObjectNode.get("StatusCode").asInt();
		} catch (ClientErrorException exception) {
			if (exception.getResponse().getStatus() == 404) {
				throw new NotFoundException(String.format("No such container %s", containerId));
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		} catch (Exception e) {
			throw new DockerException(e);
		}
	}
}
