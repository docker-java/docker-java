package com.github.dockerjava.jaxrs1.command;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.RemoveContainerCmd;
import com.github.dockerjava.client.command.AbstrDockerCmd;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.WebResource;

/**
 * Remove a container.
 * 
 * @param removeVolumes - true or false, Remove the volumes associated to the container. Defaults to false
 * @param force - true or false, Removes the container even if it was running. Defaults to false
 */
public class RemoveContainerCommand extends	AbstrDockerCmd<RemoveContainerCommand, Void> implements RemoveContainerCmd {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RemoveContainerCommand.class);

	private String containerId;

	private boolean removeVolumes, force;

	public RemoveContainerCommand(String containerId) {
		withContainerId(containerId);
	}

	@Override
	public String getContainerId() {
		return containerId;
	}

	@Override
	public boolean hasRemoveVolumesEnabled() {
		return removeVolumes;
	}

	@Override
	public boolean hasForceEnabled() {
		return force;
	}

	@Override
	public RemoveContainerCmd withContainerId(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}

	@Override
	public RemoveContainerCmd withRemoveVolumes(boolean removeVolumes) {
		this.removeVolumes = removeVolumes;
		return this;
	}

	@Override
	public RemoveContainerCmd withForce() {
		return withForce(true);
	}

	@Override
	public RemoveContainerCmd withForce(boolean force) {
		this.force = force;
		return this;
	}

	@Override
	public String toString() {
		return new StringBuilder("rm ")
				.append(removeVolumes ? "--volumes=true" : "")
				.append(force ? "--force=true" : "").append(containerId)
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
		Preconditions.checkState(!StringUtils.isEmpty(containerId),
				"Container ID can't be empty");

		WebResource webResource = baseResource
				.path("/containers/" + containerId)
				.queryParam("v", removeVolumes ? "1" : "0")
				.queryParam("force", force ? "1" : "0");

		LOGGER.trace("DELETE: {}", webResource);
		String response = webResource.accept(MediaType.APPLICATION_JSON).delete(String.class);
		LOGGER.trace("Response: {}", response);

		return null;
	}
}
