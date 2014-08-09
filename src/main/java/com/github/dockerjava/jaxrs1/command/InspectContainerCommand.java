package com.github.dockerjava.jaxrs1.command;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.client.command.AbstrDockerCmd;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.WebResource;

/**
 * Inspect the details of a container.
 */
public class InspectContainerCommand extends AbstrDockerCmd<InspectContainerCommand, InspectContainerResponse> implements InspectContainerCmd {

	private static final Logger LOGGER = LoggerFactory.getLogger(InspectContainerCommand.class);

	private String containerId;

	public InspectContainerCommand(String containerId) {
		withContainerId(containerId);
	}

    @Override
	public String getContainerId() {
        return containerId;
    }

    @Override
	public InspectContainerCmd withContainerId(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}

    @Override
    public String toString() {
        return "inspect " + containerId;
    }
    
    /**
     * @throws NotFoundException No such container
     */
	@Override
    public InspectContainerResponse exec() throws NotFoundException {
    	return super.exec();
    }

	protected InspectContainerResponse impl() throws DockerException {
		WebResource webResource = baseResource.path(String.format("/containers/%s/json", containerId));
		
		LOGGER.trace("GET: {}", webResource);
		return webResource.accept(MediaType.APPLICATION_JSON).get(InspectContainerResponse.class);
	}
}
