package com.github.dockerjava.client.command;

import static javax.ws.rs.client.Entity.entity;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.RestartContainerCmd;
import com.google.common.base.Preconditions;

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
		WebTarget webResource = baseResource.path("/containers/{id}/restart")
				.resolveTemplate("id", containerId)
				.queryParam("t", String.valueOf(timeout));
		
		LOGGER.trace("POST: {}", webResource);
		webResource.request().accept(MediaType.APPLICATION_JSON).post(entity(null, MediaType.APPLICATION_JSON_TYPE));

		return null;
	}
}
