package com.github.dockerjava.core.command;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.NotModifiedException;
import com.github.dockerjava.api.command.DockerCmdExec;
import com.github.dockerjava.api.command.StopContainerCmd;
import com.google.common.base.Preconditions;

/**
 * Stop a running container.
 *
 * @param containerId - Id of the container
 * @param timeout - Timeout in seconds before killing the container. Defaults to 10 seconds.
 *
 */
public class StopContainerCmdImpl extends AbstrDockerCmd<StopContainerCmd, Void> implements StopContainerCmd {

	private String containerId;

	private int timeout = 10;

	public StopContainerCmdImpl(DockerCmdExec<StopContainerCmd, Void> exec, String containerId) {
		super(exec);
		withContainerId(containerId);
	}

    @Override
	public String getContainerId() {
        return containerId;
    }

    @Override
	public int getTimeout() {
        return timeout;
    }

    @Override
	public StopContainerCmd withContainerId(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}

	@Override
	public StopContainerCmd withTimeout(int timeout) {
		Preconditions.checkArgument(timeout >= 0, "timeout must be greater or equal 0");
		this.timeout = timeout;
		return this;
	}

    @Override
    public String toString() {
        return new StringBuilder("stop ")
            .append("--time=" + timeout + " ")
            .append(containerId)
            .toString();
    }
    
    /**
	 * @throws NotFoundException No such container
	 * @throws NotModifiedException Container already stopped
	 */
	@Override
	public Void exec() throws NotFoundException, NotModifiedException {
		return super.exec();
	}

//	protected Void impl() throws DockerException {
//		WebTarget webResource = baseResource.path("/containers/{id}/stop")
//				.resolveTemplate("id", containerId)
//				.queryParam("t", String.valueOf(timeout));
//		
//		LOGGER.trace("POST: {}", webResource);
//		webResource.request().accept(MediaType.APPLICATION_JSON).post(entity(null, MediaType.APPLICATION_JSON));
//
//		return null;
//	}
}
