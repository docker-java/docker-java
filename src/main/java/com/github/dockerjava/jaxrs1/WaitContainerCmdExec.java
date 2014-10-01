package com.github.dockerjava.jaxrs1;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dockerjava.api.command.WaitContainerCmd;
import com.sun.jersey.api.client.WebResource;

public class WaitContainerCmdExec extends AbstrDockerCmdExec<WaitContainerCmd, Integer> implements WaitContainerCmd.Exec {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(WaitContainerCmdExec.class);

	public WaitContainerCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected Integer execute(WaitContainerCmd command) {
		WebResource webResource = getBaseResource()
		        .resolveTemplate("/containers/{id}/wait", command.getContainerId())
		        .build();

		LOGGER.trace("POST: {}", webResource);
		ObjectNode ObjectNode = webResource.accept(MediaType.APPLICATION_JSON)
				.entity(null, MediaType.TEXT_PLAIN)
				.post(ObjectNode.class);
		
        return ObjectNode.get("StatusCode").asInt();
	}

}
