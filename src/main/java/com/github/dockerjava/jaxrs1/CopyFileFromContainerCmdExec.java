package com.github.dockerjava.jaxrs1;

import java.io.InputStream;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.CopyFileFromContainerCmd;
import com.sun.jersey.api.client.WebResource;

public class CopyFileFromContainerCmdExec extends AbstrDockerCmdExec<CopyFileFromContainerCmd, InputStream> implements CopyFileFromContainerCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CopyFileFromContainerCmdExec.class);
	
	public CopyFileFromContainerCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected InputStream execute(CopyFileFromContainerCmd command) {
		WebResource webResource = getBaseResource()
		        .resolveTemplate("/containers/{id}/copy", command.getContainerId())
		        .build();

		LOGGER.trace("POST: " + webResource.toString());
		
		return webResource
		        .accept(MediaType.APPLICATION_OCTET_STREAM_TYPE)
		        .entity(command, MediaType.APPLICATION_JSON)
		        .post(InputStream.class);		
	}

}
