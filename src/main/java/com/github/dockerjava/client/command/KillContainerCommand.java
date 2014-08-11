package com.github.dockerjava.client.command;

import static javax.ws.rs.client.Entity.entity;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.KillContainerCmd;
import com.google.common.base.Preconditions;

/**
 * Kill a running container.
 */
public class KillContainerCommand extends AbstrDockerCmd<KillContainerCommand, Void> implements KillContainerCmd {

	private static final Logger LOGGER = LoggerFactory.getLogger(KillContainerCommand.class);

	private String containerId, signal;

	public KillContainerCommand(String containerId) {
		withContainerId(containerId);
	}

    @Override
	public String getContainerId() {
        return containerId;
    }

    @Override
	public String getSignal() {
        return signal;
    }

    @Override
	public KillContainerCmd withContainerId(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}

	@Override
	public KillContainerCmd withSignal(String signal) {
		Preconditions.checkNotNull(signal, "signal was not specified");
		this.signal = signal;
		return this;
	}

    @Override
    public String toString() {
        return "kill " + containerId;
    }
    
    /**
     * @throws NotFoundException No such container
     */
	@Override
    public Void exec() throws NotFoundException {
    	return super.exec();
    }

	protected Void impl() throws DockerException {
		WebTarget webResource = baseResource.path("/containers/{id}/kill").resolveTemplate("id", containerId);

		if(signal != null) {
			webResource = webResource.queryParam("signal", signal);
		}
	
		LOGGER.trace("POST: {}", webResource);
		webResource.request().accept(MediaType.APPLICATION_JSON).post(entity(null, MediaType.APPLICATION_JSON));	

		return null;
	}
}
