package com.github.dockerjava.jaxrs;

import static javax.ws.rs.client.Entity.entity;

import java.io.InputStream;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.CopyFileFromContainerCmd;

public class CopyFileFromContainerCmdExec extends AbstrDockerCmdExec<CopyFileFromContainerCmd, InputStream> implements CopyFileFromContainerCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CopyFileFromContainerCmdExec.class);
	
	public CopyFileFromContainerCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected InputStream execute(CopyFileFromContainerCmd command) {
		WebTarget webResource = getBaseResource()
				.path("/containers/{id}/copy")
				.resolveTemplate("id", command.getContainerId());

		LOGGER.trace("POST: " + webResource.toString());
		
		return webResource.request().accept(MediaType.APPLICATION_OCTET_STREAM_TYPE).post(entity(command, MediaType.APPLICATION_JSON)).readEntity(InputStream.class);		
	}

}
