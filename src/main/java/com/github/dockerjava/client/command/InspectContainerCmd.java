package com.github.dockerjava.client.command;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.NotFoundException;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.WebResource;

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
