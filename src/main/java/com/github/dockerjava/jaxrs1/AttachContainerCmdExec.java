package com.github.dockerjava.jaxrs1;

import java.io.InputStream;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.AttachContainerCmd;
import com.sun.jersey.api.client.WebResource;

public class AttachContainerCmdExec extends AbstrDockerCmdExec<AttachContainerCmd, InputStream> implements AttachContainerCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AttachContainerCmdExec.class);
	
	public AttachContainerCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected InputStream execute(AttachContainerCmd command) {
	    WebResource webResource = getBaseResource()
	            .resolveTemplate("/containers/{id}/attach", command.getContainerId())
                .queryParam("logs", command.hasLogsEnabled() ? "1" : "0")
                .queryParam("stdout", command.hasStdoutEnabled() ? "1" : "0")
                .queryParam("stderr", command.hasStderrEnabled() ? "1" : "0")
                .queryParam("stream", command.hasFollowStreamEnabled() ? "1" : "0")
                .build();

		LOGGER.trace("POST: {}", webResource);
		
		return webResource.accept(MediaType.APPLICATION_OCTET_STREAM_TYPE)
				.entity(null, MediaType.APPLICATION_JSON)
				.post(InputStream.class);
	}

}
