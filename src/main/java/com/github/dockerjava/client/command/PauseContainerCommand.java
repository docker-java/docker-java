package com.github.dockerjava.client.command;

import static javax.ws.rs.client.Entity.entity;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.PauseContainerCmd;
import com.google.common.base.Preconditions;

/**
 * Pause a container.
 *
 * @param containerId - Id of the container
 *
 */
public class PauseContainerCommand extends AbstrDockerCmd<PauseContainerCommand, Void> implements PauseContainerCmd {

	private static final Logger LOGGER = LoggerFactory.getLogger(PauseContainerCommand.class);

	private String containerId;

	public PauseContainerCommand(String containerId) {
		withContainerId(containerId);
	}

    @Override
	public String getContainerId() {
        return containerId;
    }

    @Override
	public PauseContainerCmd withContainerId(String containerId) {
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
	
	/**
	 * @throws NotFoundException No such container
	 */
	@Override
	public Void exec() throws NotFoundException {
		return super.exec();
	}

	protected Void impl() throws DockerException {
		WebTarget webResource = baseResource.path("/containers/{id}/pause")
				.resolveTemplate("id", containerId);
		
		LOGGER.trace("POST: {}", webResource);
		webResource.request()
				.accept(MediaType.APPLICATION_JSON)
				.post(entity(null, MediaType.APPLICATION_JSON), Response.class);

		return null;
	}
}
