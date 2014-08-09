package com.github.dockerjava.jaxrs1.command;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.InternalServerErrorException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.ContainerDiffCmd;
import com.github.dockerjava.client.command.AbstrDockerCmd;
import com.github.dockerjava.client.model.ChangeLog;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

/**
 * Inspect changes on a container's filesystem
 *
 * @param containerId - Id of the container
 *
 */
public class ContainerDiffCommand extends AbstrDockerCmd<ContainerDiffCommand, List<ChangeLog>> implements ContainerDiffCmd {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContainerDiffCommand.class);

	private String containerId;

	public ContainerDiffCommand(String containerId) {
		withContainerId(containerId);
	}

    @Override
	public String getContainerId() {
        return containerId;
    }

    @Override
	public ContainerDiffCommand withContainerId(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}

	@Override
    public String toString() {
        return new StringBuilder("diff ").append(containerId).toString();
    }
    
    /**
     * @throws NotFoundException No such container
     * @throws InternalServerErrorException server error
     * @throws DockerException unexpected http status code
     */
	@Override
    public List<ChangeLog> exec() throws NotFoundException {
    	return super.exec();
    }

    protected List<ChangeLog> impl() throws DockerException {
		WebResource webResource = baseResource.path(String.format("/containers/%s/changes", containerId));
		
		LOGGER.trace("GET: {}", webResource);
		return webResource.accept(MediaType.APPLICATION_JSON).get(new GenericType<List<ChangeLog>>() {
		});
	}
}
