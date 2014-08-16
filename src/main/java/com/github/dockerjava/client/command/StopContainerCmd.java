package com.github.dockerjava.client.command;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.google.common.base.Preconditions;
import javax.ws.rs.client.WebTarget;

import static javax.ws.rs.client.Entity.entity;

/**
 * Stop a running container.
 *
 * @param containerId - Id of the container
 * @param timeout - Timeout in seconds before killing the container. Defaults to 10 seconds.
 *
 */
public class StopContainerCmd extends AbstrDockerCmd<StopContainerCmd, Void> {

	private static final Logger LOGGER = LoggerFactory.getLogger(StopContainerCmd.class);

	private String containerId;

	private int timeout = 10;

	public StopContainerCmd(String containerId) {
		withContainerId(containerId);
	}

    public String getContainerId() {
        return containerId;
    }

    public int getTimeout() {
        return timeout;
    }

    public StopContainerCmd withContainerId(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}

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

	protected Void impl() throws DockerException {
		WebTarget webResource = baseResource.path("/containers/{id}/stop").resolveTemplate("id", containerId)
				.queryParam("t", String.valueOf(timeout));

		try {
			LOGGER.trace("POST: {}", webResource);
			webResource.request().accept(MediaType.APPLICATION_JSON).post(entity(null, MediaType.APPLICATION_JSON));
		} catch (ClientErrorException exception) {
			if (exception.getResponse().getStatus() == 404) {
				LOGGER.warn("No such container {}", containerId);
			} else if(exception.getResponse().getStatus() == 304) {
				//no error
				LOGGER.warn("Container already stopped {}", containerId);
			} else if (exception.getResponse().getStatus() == 204) {
				//no error
				LOGGER.trace("Successfully stopped container {}", containerId);
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}

		return null;
	}
}
