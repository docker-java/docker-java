package com.github.dockerjava.client.command;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

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
		WebResource webResource = baseResource.path(String.format("/containers/%s/stop", containerId))
				.queryParam("t", String.valueOf(timeout));

		try {
			LOGGER.trace("POST: {}", webResource);
			webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post();
		} catch (UniformInterfaceException exception) {
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
