package com.github.dockerjava.jaxrs;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.RemoveContainerCmd;

public class RemoveContainerCmdExec extends AbstrDockerCmdExec<RemoveContainerCmd, Void> implements RemoveContainerCmd.Exec {

	private static final Logger LOGGER = LoggerFactory.getLogger(RemoveContainerCmdExec.class);

	public RemoveContainerCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected Void execute(RemoveContainerCmd command) {
		WebTarget webResource = getBaseResource().path("/containers/" + command.getContainerId())
				.queryParam("v", command.hasRemoveVolumesEnabled() ? "1" : "0")
				.queryParam("force", command.hasForceEnabled() ? "1" : "0");
		
		LOGGER.trace("DELETE: {}", webResource);
		webResource.request().accept(MediaType.APPLICATION_JSON).delete().close();

		return null;
	}

}
