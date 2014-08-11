package com.github.dockerjava.client.command;

import static javax.ws.rs.client.Entity.entity;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dockerjava.api.command.WaitContainerCmd;
import com.google.common.base.Preconditions;

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
		WebTarget webResource = baseResource.path("/containers/{id}/wait")
				.resolveTemplate("id", containerId);

		LOGGER.trace("POST: {}", webResource);
		ObjectNode ObjectNode = webResource.request().accept(MediaType.APPLICATION_JSON)
				.post(entity(null, MediaType.APPLICATION_JSON), ObjectNode.class);
		
        return ObjectNode.get("StatusCode").asInt();
	}
}
