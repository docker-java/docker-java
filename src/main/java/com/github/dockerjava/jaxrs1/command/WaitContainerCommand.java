package com.github.dockerjava.jaxrs1.command;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dockerjava.api.command.WaitContainerCmd;
import com.github.dockerjava.client.command.AbstrDockerCmd;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.WebResource;

/**
 * Wait a container
 * 
 * Block until container stops, then returns its exit code
 */
public class WaitContainerCommand extends AbstrDockerCmd<WaitContainerCommand, Integer> implements WaitContainerCmd {

	private static final Logger LOGGER = LoggerFactory.getLogger(WaitContainerCommand.class);

	private String containerId;

	public WaitContainerCommand(String containerId) {
		withContainerId(containerId);
	}

    @Override
	public String getContainerId() {
        return containerId;
    }

    @Override
	public WaitContainerCmd withContainerId(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}

    @Override
    public String toString() {
        return "wait " + containerId;
    }

	protected Integer impl() {
		WebResource webResource = baseResource.path(String.format("/containers/%s/wait", containerId));

		LOGGER.trace("POST: {}", webResource);
		ObjectNode ObjectNode = webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).post(ObjectNode.class);

		return ObjectNode.get("StatusCode").asInt();
	}
}
