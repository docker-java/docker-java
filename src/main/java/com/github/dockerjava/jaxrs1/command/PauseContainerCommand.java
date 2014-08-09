package com.github.dockerjava.jaxrs1.command;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.PauseContainerCmd;
import com.github.dockerjava.client.command.AbstrDockerCmd;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

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
		WebResource webResource = baseResource.path(String.format("/containers/%s/pause", containerId));

		LOGGER.trace("POST: {}", webResource);
		webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(ClientResponse.class);

		return null;
	}
}
