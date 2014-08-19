package com.github.dockerjava.core.command;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.DockerCmdExec;
import com.github.dockerjava.api.command.PauseContainerCmd;
import com.google.common.base.Preconditions;

/**
 * Pause a container.
 *
 * @param containerId - Id of the container
 *
 */
public class PauseContainerCmdImpl extends AbstrDockerCmd<PauseContainerCmd, Void> implements PauseContainerCmd {

	private String containerId;

	public PauseContainerCmdImpl(DockerCmdExec<PauseContainerCmd, Void> exec, String containerId) {
		super(exec);
		withContainerId(containerId);
	}

    @Override
	public String getContainerId() {
        return containerId;
    }

    @Override
	public PauseContainerCmd withContainerId(String containerId) {
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
//		WebTarget webResource = baseResource.path("/containers/{id}/pause")
//				.resolveTemplate("id", containerId);
//		
//		LOGGER.trace("POST: {}", webResource);
//		webResource.request()
//				.accept(MediaType.APPLICATION_JSON)
//				.post(entity(null, MediaType.APPLICATION_JSON), Response.class);
//
//		return null;
//	}
}
