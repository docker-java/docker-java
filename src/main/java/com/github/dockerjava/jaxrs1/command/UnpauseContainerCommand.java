package com.github.dockerjava.jaxrs1.command;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.UnpauseContainerCmd;
import com.github.dockerjava.client.command.AbstrDockerCmd;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Unpause a container.
 *
 * @param containerId - Id of the container
 *
 */
public class UnpauseContainerCommand extends AbstrDockerCmd<UnpauseContainerCommand, Void> implements UnpauseContainerCmd {

	private static final Logger LOGGER = LoggerFactory.getLogger(UnpauseContainerCommand.class);

	private String containerId;

	public UnpauseContainerCommand(String containerId) {
		withContainerId(containerId);
	}

    @Override
	public String getContainerId() {
        return containerId;
    }

    @Override
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
	
	/**
	 * @throws NotFoundException No such container
	 */
	@Override
	public Void exec() throws NotFoundException {
		return super.exec();
	}

	protected Void impl() throws DockerException {
		WebResource webResource = baseResource.path(String.format("/containers/%s/unpause", containerId));


		LOGGER.trace("POST: {}", webResource);
		webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(ClientResponse.class);

		return null;
	}
}
