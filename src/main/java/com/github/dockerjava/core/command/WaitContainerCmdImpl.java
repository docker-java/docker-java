package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.DockerCmdExec;
import com.github.dockerjava.api.command.WaitContainerCmd;
import com.google.common.base.Preconditions;

/**
 * Wait a container
 * 
 * Block until container stops, then returns its exit code
 */
public class WaitContainerCmdImpl extends AbstrDockerCmd<WaitContainerCmd, Integer> implements WaitContainerCmd {

	private String containerId;

	public WaitContainerCmdImpl(DockerCmdExec<WaitContainerCmd, Integer> exec, String containerId) {
		super(exec);
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

//	protected Integer impl() {
//		WebTarget webResource = baseResource.path("/containers/{id}/wait")
//				.resolveTemplate("id", containerId);
//
//		LOGGER.trace("POST: {}", webResource);
//		ObjectNode ObjectNode = webResource.request().accept(MediaType.APPLICATION_JSON)
//				.post(entity(null, MediaType.APPLICATION_JSON), ObjectNode.class);
//		
//        return ObjectNode.get("StatusCode").asInt();
//	}
}
