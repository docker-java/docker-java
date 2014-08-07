package com.github.dockerjava.client.command;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dockerjava.api.DockerException;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.WebResource;

/**
 * Wait a container
 * 
 * Block until container stops, then returns the exit code
 */
public class WaitContainerCmd extends AbstrDockerCmd<WaitContainerCmd, Integer> {

	private static final Logger LOGGER = LoggerFactory.getLogger(WaitContainerCmd.class);

	private String containerId;

	public WaitContainerCmd(String containerId) {
		withContainerId(containerId);
	}

    public String getContainerId() {
        return containerId;
    }

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
