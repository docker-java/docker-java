package com.github.dockerjava.client.command;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.NotFoundException;
import com.google.common.base.Preconditions;
import javax.ws.rs.client.WebTarget;

import static javax.ws.rs.client.Entity.entity;

/**
 * Restart a running container.
 *
 * @param timeout - Timeout in seconds before killing the container. Defaults to 10 seconds.
 *
 */
public class RestartContainerCmd extends AbstrDockerCmd<RestartContainerCmd, Void> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestartContainerCmd.class);

	private String containerId;

	private int timeout = 10;

	public RestartContainerCmd(String containerId) {
		withContainerId(containerId);
	}

    public String getContainerId() {
        return containerId;
    }

    public int getTimeout() {
        return timeout;
    }

    public RestartContainerCmd withContainerId(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}

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

	protected Void impl() throws DockerException {
		WebTarget webResource = baseResource.path("/containers/{id}/restart").resolveTemplate("id", containerId)
				.queryParam("t", String.valueOf(timeout));

		try {
			LOGGER.trace("POST: {}", webResource);
			webResource.request().accept(MediaType.APPLICATION_JSON).post(entity(null, MediaType.APPLICATION_JSON_TYPE));
		} catch (ClientErrorException exception) {
			if (exception.getResponse().getStatus() == 404) {
				throw new NotFoundException(String.format("No such container %s", containerId));
			} else if (exception.getResponse().getStatus() == 204) {
				//no error
				LOGGER.trace("Successfully restarted container {}", containerId);
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}

		return null;
	}
}
