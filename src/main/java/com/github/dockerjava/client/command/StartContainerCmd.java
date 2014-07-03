package com.github.dockerjava.client.command;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.NotFoundException;
import com.github.dockerjava.client.model.Bind;
import com.github.dockerjava.client.model.LxcConf;
import com.github.dockerjava.client.model.Ports;
import com.github.dockerjava.client.model.StartContainerConfig;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

/**
 * 
 * 
 * 
 */
public class StartContainerCmd extends AbstrDockerCmd<StartContainerCmd, Void> {

	private static final Logger LOGGER = LoggerFactory.getLogger(StartContainerCmd.class);

	private String containerId;
	
	private StartContainerConfig startContainerConfig;
	
	public StartContainerCmd(String containerId) {
		startContainerConfig = new StartContainerConfig();
		withContainerId(containerId);
	}
	
	public StartContainerCmd withBinds(Bind... binds) {
		startContainerConfig.setBinds(binds);
		return this;
	}

	public StartContainerCmd withLxcConf(LxcConf[] lxcConf) {
		startContainerConfig.setLxcConf(lxcConf);
		return this;
	}

	public StartContainerCmd withPortBindings(Ports portBindings) {
		startContainerConfig.setPortBindings(portBindings);
		return this;
	}

	public StartContainerCmd withPrivileged(boolean privileged) {
		startContainerConfig.setPrivileged(privileged);
		return this;
	}

	public StartContainerCmd withPublishAllPorts(boolean publishAllPorts) {
		startContainerConfig.setPublishAllPorts(publishAllPorts);
		return this;
	}

	public StartContainerCmd withDns(String dns) {
		startContainerConfig.setDns(dns);
		return this;
	}


	public StartContainerCmd withVolumesFrom(String volumesFrom) {
		startContainerConfig.setVolumesFrom(volumesFrom);
		return this;
	}
	
	public StartContainerCmd withContainerId(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}
	
	protected Void impl() throws DockerException {
		WebResource webResource = baseResource.path(String.format("/containers/%s/start", containerId));

		try {
			LOGGER.trace("POST: {}", webResource);
			Builder builder = webResource.accept(MediaType.TEXT_PLAIN);
			if (startContainerConfig != null) {
				builder.type(MediaType.APPLICATION_JSON).post(startContainerConfig);
			} else {
				builder.post((StartContainerConfig) null);
			}
			
			
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 404) {
				throw new NotFoundException(String.format("No such container %s", containerId));
			} else if (exception.getResponse().getStatus() == 204) {
				//no error
				LOGGER.trace("Successfully started container {}", containerId);
			} else if (exception.getResponse().getStatus() == 500) {
				LOGGER.error("", exception);
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}
		
		return null;
	}
}
