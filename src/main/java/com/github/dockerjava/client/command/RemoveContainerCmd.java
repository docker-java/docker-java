package com.github.dockerjava.client.command;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

/**
 * Remove a container.
 * 
 * @param removeVolumes - true or false, Remove the volumes associated to the container. Defaults to false
 * @param force - true or false, Removes the container even if it was running. Defaults to false
 */
public class RemoveContainerCmd extends AbstrDockerCmd<RemoveContainerCmd, Void> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RemoveContainerCmd.class);

	private String containerId;
	
	private boolean removeVolumes, force;
	
	public RemoveContainerCmd(String containerId) {
		withContainerId(containerId);
	}
	
	public RemoveContainerCmd withContainerId(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}
	
	public RemoveContainerCmd withRemoveVolumes(boolean removeVolumes) {
		this.removeVolumes = removeVolumes;
		return this;
	}
	
	public RemoveContainerCmd withForce() {
		return withForce(true);
	}
	
	public RemoveContainerCmd withForce(boolean force) {
		this.force = force;
		return this;
	}
	
    @Override
    public String toString() {
        return new StringBuilder("rm ")
            .append(removeVolumes ? "--volumes=true" : "")
            .append(force ? "--force=true" : "")
            .append(containerId)
            .toString();
    }   

	protected Void impl() throws DockerException {
		Preconditions.checkState(!StringUtils.isEmpty(containerId), "Container ID can't be empty");

		WebResource webResource = baseResource.path("/containers/" + containerId).queryParam("v", removeVolumes ? "1" : "0").queryParam("force", force ? "1" : "0");

		try {
			LOGGER.trace("DELETE: {}", webResource);
			String response = webResource.accept(MediaType.APPLICATION_JSON).delete(String.class);
			LOGGER.trace("Response: {}", response);
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 204) {
				//no error
				LOGGER.trace("Successfully removed container " + containerId);
			} else if (exception.getResponse().getStatus() == 400) {
				throw new DockerException("bad parameter");
			} else if (exception.getResponse().getStatus() == 404) {
				// should really throw a NotFoundException instead of silently ignoring the problem
				LOGGER.warn(String.format("%s is an unrecognized container.", containerId));
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}
		
		return null;
	}
}
