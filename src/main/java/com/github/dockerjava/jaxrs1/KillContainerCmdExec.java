package com.github.dockerjava.jaxrs1;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.KillContainerCmd;
import com.sun.jersey.api.client.WebResource;

public class KillContainerCmdExec extends AbstrDockerCmdExec<KillContainerCmd, Void> implements KillContainerCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(KillContainerCmdExec.class);
	
	public KillContainerCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected Void execute(KillContainerCmd command) {
		WebResource webResource = getBaseResource()
		        .resolveTemplate("/containers/{id}/kill", command.getContainerId())
		        .build();

		if(command.getSignal() != null) {
			webResource = webResource.queryParam("signal", command.getSignal());
		}
	
		LOGGER.trace("POST: {}", webResource);
		webResource.accept(MediaType.APPLICATION_JSON)
		    .post();	

		return null;
	}

}
