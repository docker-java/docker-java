package com.github.dockerjava.core.command;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.DockerCmdExec;
import com.github.dockerjava.api.command.UnpauseContainerCmd;
import com.google.common.base.Preconditions;

/**
 * Unpause a container.
 *
 * @param containerId - Id of the container
 *
 */
public class UnpauseContainerCmdImpl extends AbstrDockerCmd<UnpauseContainerCmd, Void> implements UnpauseContainerCmd {

	private String containerId;

	public UnpauseContainerCmdImpl(DockerCmdExec<UnpauseContainerCmd, Void> exec, String containerId) {
		super(exec);
		withContainerId(containerId);
	}

    @Override
	public String getContainerId() {
        return containerId;
    }

    @Override
	public UnpauseContainerCmd withContainerId(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}

	@Override
    public String toString() {
        return new StringBuilder("pause ")
            .append(containerId)
            .toString();
    }
	
	/**
	 * @throws NotFoundException No such container
	 */
	@Override
	public Void exec() throws NotFoundException {
		return super.exec();
	}

//	protected Void impl() throws DockerException {
//		WebTarget webResource = baseResource.path("/containers/{id}/unpause")
//				.resolveTemplate("id", containerId);
//		
//		LOGGER.trace("POST: {}", webResource);
//		webResource.request().accept(MediaType.APPLICATION_JSON)
//				.post(Entity.entity(Response.class, MediaType.APPLICATION_JSON));
//
//		return null;
//	}
}
