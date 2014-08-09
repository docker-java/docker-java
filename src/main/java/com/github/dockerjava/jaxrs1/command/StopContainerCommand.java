package com.github.dockerjava.jaxrs1.command;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.NotModifiedException;
import com.github.dockerjava.api.command.StopContainerCmd;
import com.github.dockerjava.client.command.AbstrDockerCmd;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.WebResource;

/**
 * Stop a running container.
 *
 * @param containerId - Id of the container
 * @param timeout - Timeout in seconds before killing the container. Defaults to 10 seconds.
 *
 */
public class StopContainerCommand extends AbstrDockerCmd<StopContainerCommand, Void> implements StopContainerCmd {

	private static final Logger LOGGER = LoggerFactory.getLogger(StopContainerCommand.class);

	private String containerId;

	private int timeout = 10;

	public StopContainerCommand(String containerId) {
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

	protected Void impl() throws DockerException {
		WebResource webResource = baseResource.path(
				String.format("/containers/%s/stop", containerId)).queryParam(
				"t", String.valueOf(timeout));
		
		LOGGER.trace("POST: {}", webResource);
		webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post();

		return null;
	}
}
