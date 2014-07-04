package com.github.dockerjava.client.command;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.NotFoundException;
import com.github.dockerjava.client.model.ChangeLog;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

/**
 * Inspect changes on a container's filesystem
 * 
 * @param containerId - Id of the container
 * 
 */
public class ContainerDiffCmd extends AbstrDockerCmd<ContainerDiffCmd, List<ChangeLog>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContainerDiffCmd.class);

	private String containerId;
	
	public ContainerDiffCmd(String containerId) {
		withContainerId(containerId);
	}
	
	public ContainerDiffCmd withContainerId(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}
	
    @Override
    public String toString() {
        return new StringBuilder("diff ")
            .append(containerId)
            .toString();
    }

    protected List<ChangeLog> impl() throws DockerException {
		WebResource webResource = baseResource.path(String.format("/containers/%s/changes", containerId));

		try {
			LOGGER.trace("GET: {}", webResource);
			return webResource.accept(MediaType.APPLICATION_JSON).get(new GenericType<List<ChangeLog>>() {
			});
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
