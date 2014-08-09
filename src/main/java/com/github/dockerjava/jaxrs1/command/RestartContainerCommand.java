package com.github.dockerjava.jaxrs1.command;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.RestartContainerCmd;
import com.github.dockerjava.client.command.AbstrDockerCmd;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.WebResource;

/**
 * Restart a running container.
 *
 * @param timeout - Timeout in seconds before killing the container. Defaults to 10 seconds.
 *
 */
public class RestartContainerCommand extends AbstrDockerCmd<RestartContainerCommand, Void> implements RestartContainerCmd {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestartContainerCommand.class);

	private String containerId;

	private int timeout = 10;

	public RestartContainerCommand(String containerId) {
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
	public RestartContainerCmd withContainerId(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}

	@Override
	public RestartContainerCmd withtTimeout(int timeout) {
		Preconditions.checkArgument(timeout >= 0, "timeout must be greater or equal 0");
		this.timeout = timeout;
		return this;
	}

    @Override
    public String toString() {
        return new StringBuilder("restart ")
            .append("--time=" + timeout + " ")
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

	protected Void impl() throws DockerException {
		WebResource webResource = baseResource.path(
				String.format("/containers/%s/restart", containerId))
				.queryParam("t", String.valueOf(timeout));
		
		LOGGER.trace("POST: {}", webResource);
		webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post();
	
		return null;
	}
}
