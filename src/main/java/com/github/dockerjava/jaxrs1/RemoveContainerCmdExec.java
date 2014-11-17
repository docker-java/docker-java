package com.github.dockerjava.jaxrs1;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.RemoveContainerCmd;
import com.sun.jersey.api.client.WebResource;

public class RemoveContainerCmdExec extends AbstrDockerCmdExec<RemoveContainerCmd, Void> implements RemoveContainerCmd.Exec {

	private static final Logger LOGGER = LoggerFactory.getLogger(RemoveContainerCmdExec.class);

	public RemoveContainerCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected Void execute(RemoveContainerCmd command) {
		WebResource webResource = getBaseResource()
		        .path("/containers/" + command.getContainerId())
				.queryParam("v", command.hasRemoveVolumesEnabled() ? "1" : "0")
				.queryParam("force", command.hasForceEnabled() ? "1" : "0")
				.build();
		
		LOGGER.trace("DELETE: {}", webResource);
		webResource.accept(MediaType.APPLICATION_JSON).delete();

		return null;
	}

}
