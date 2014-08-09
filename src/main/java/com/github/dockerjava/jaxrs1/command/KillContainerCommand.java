package com.github.dockerjava.jaxrs1.command;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.KillContainerCmd;
import com.github.dockerjava.client.command.AbstrDockerCmd;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.WebResource;

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
		WebResource webResource = baseResource.path(String.format("/containers/%s/kill", containerId));

		if(signal != null) {
			webResource = webResource.queryParam("signal", signal);
		}
		
		LOGGER.trace("POST: {}", webResource);
		webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post();

		return null;
	}
}
