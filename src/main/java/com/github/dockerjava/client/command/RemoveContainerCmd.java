package com.github.dockerjava.client.command;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.NotFoundException;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.WebResource;

/**
 * Remove a container.
 * 
 * @param removeVolumes - true or false, Remove the volumes associated to the container. Defaults to false
 * @param force - true or false, Removes the container even if it was running. Defaults to false
 */
public class RemoveContainerCmd extends	AbstrDockerCmd<RemoveContainerCmd, Void> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RemoveContainerCmd.class);

	private String containerId;

	private boolean removeVolumes, force;

	public RemoveContainerCmd(String containerId) {
		withContainerId(containerId);
	}

	public String getContainerId() {
		return containerId;
	}

	public boolean hasRemoveVolumesEnabled() {
		return removeVolumes;
	}

	public boolean hasForceEnabled() {
		return force;
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
