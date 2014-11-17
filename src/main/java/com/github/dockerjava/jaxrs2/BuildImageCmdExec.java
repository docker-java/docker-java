package com.github.dockerjava.jaxrs2;

import static javax.ws.rs.client.Entity.entity;

import java.io.InputStream;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.BuildImageCmd;

public class BuildImageCmdExec extends AbstrDockerCmdExec<BuildImageCmd, InputStream> implements BuildImageCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BuildImageCmdExec.class);
	
	public BuildImageCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected InputStream execute(BuildImageCmd command) {
		WebTarget webResource = getBaseResource().path("/build");
		
		if(command.getTag() != null) {
			webResource = webResource.queryParam("t", command.getTag());
		}
        if (command.hasNoCacheEnabled()) {
            webResource = webResource.queryParam("nocache", "true");
        }
        if (command.hasRemoveEnabled()) {
            webResource = webResource.queryParam("rm", "true");
        }
        if (command.isQuiet()) {
            webResource = webResource.queryParam("q", "true");
        }
		
		LOGGER.debug("POST: {}", webResource);
		return webResource
                .request()
				.accept(MediaType.TEXT_PLAIN)
				.post(entity(command.getTarInputStream(), "application/tar"), Response.class).readEntity(InputStream.class);
		
	}

}
