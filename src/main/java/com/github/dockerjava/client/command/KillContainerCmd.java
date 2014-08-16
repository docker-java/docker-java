package com.github.dockerjava.client.command;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.google.common.base.Preconditions;
import javax.ws.rs.client.WebTarget;

import static javax.ws.rs.client.Entity.entity;

/**
 * Kill a running container.
 */
public class KillContainerCmd extends AbstrDockerCmd<KillContainerCmd, Void> {

	private static final Logger LOGGER = LoggerFactory.getLogger(KillContainerCmd.class);

	private String containerId, signal;

	public KillContainerCmd(String containerId) {
		withContainerId(containerId);
	}

    public String getContainerId() {
        return containerId;
    }

    public String getSignal() {
        return signal;
    }

    public KillContainerCmd withContainerId(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}

	public KillContainerCmd withSignal(String signal) {
		Preconditions.checkNotNull(signal, "signal was not specified");
		this.signal = signal;
		return this;
	}

    @Override
    public String toString() {
        return "kill " + containerId;
    }

	protected Void impl() throws DockerException {
		WebTarget webResource = baseResource.path("/containers/{id}/kill").resolveTemplate("id", containerId);

		if(signal != null) {
			webResource = webResource.queryParam("signal", signal);
		}

		try {
			LOGGER.trace("POST: {}", webResource);
			webResource.request().accept(MediaType.APPLICATION_JSON).post(entity(null, MediaType.APPLICATION_JSON));
		} catch (ClientErrorException exception) {
			if (exception.getResponse().getStatus() == 404) {
				LOGGER.warn("No such container {}", containerId);
			} else if (exception.getResponse().getStatus() == 204) {
				//no error
				LOGGER.trace("Successfully killed container {}", containerId);
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}

		return null;
	}
}
