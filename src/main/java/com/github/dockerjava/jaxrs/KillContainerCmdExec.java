package com.github.dockerjava.jaxrs;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.KillContainerCmd;

public class KillContainerCmdExec extends
		AbstrDockerCmdExec<KillContainerCmd, Void> implements
		KillContainerCmd.Exec {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(KillContainerCmdExec.class);

	public KillContainerCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected Void execute(KillContainerCmd command) {
		WebTarget webResource = getBaseResource().path("/containers/{id}/kill")
				.resolveTemplate("id", command.getContainerId());

		if (command.getSignal() != null) {
			webResource = webResource.queryParam("signal", command.getSignal());
		}

		LOGGER.trace("POST: {}", webResource);
		webResource.request().accept(MediaType.APPLICATION_JSON).post(null).close();

		return null;
	}

}
