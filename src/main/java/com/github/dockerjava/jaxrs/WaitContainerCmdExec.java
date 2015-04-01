package com.github.dockerjava.jaxrs;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dockerjava.api.command.WaitContainerCmd;

public class WaitContainerCmdExec extends AbstrDockerCmdExec<WaitContainerCmd, Integer> implements WaitContainerCmd.Exec {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(WaitContainerCmdExec.class);

	public WaitContainerCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected Integer execute(WaitContainerCmd command) {
		WebTarget webResource = getBaseResource().path("/containers/{id}/wait")
				.resolveTemplate("id", command.getContainerId());

		LOGGER.trace("POST: {}", webResource);
		ObjectNode ObjectNode = webResource.request().accept(MediaType.APPLICATION_JSON)
				.post(null, ObjectNode.class);
		
        return ObjectNode.get("StatusCode").asInt();
	}

}
