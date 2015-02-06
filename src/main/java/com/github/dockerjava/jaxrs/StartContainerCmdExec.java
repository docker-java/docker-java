package com.github.dockerjava.jaxrs;

import static javax.ws.rs.client.Entity.entity;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.StartContainerCmd;

public class StartContainerCmdExec extends AbstrDockerCmdExec<StartContainerCmd, Void> implements StartContainerCmd.Exec {

	private static final Logger LOGGER = LoggerFactory.getLogger(StartContainerCmdExec.class);

	public StartContainerCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected Void execute(StartContainerCmd command) {
		WebTarget webResource = getBaseResource().path("/containers/{id}/start").resolveTemplate("id", command.getContainerId());

		LOGGER.trace("POST: {}", webResource);
		webResource.request().accept(MediaType.APPLICATION_JSON).post(entity(command, MediaType.APPLICATION_JSON)).close();
		
		return null;
	}

}
