package com.github.dockerjava.jaxrs1;

import java.io.InputStream;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.BuildImageCmd;
import com.sun.jersey.api.client.WebResource;

public class BuildImageCmdExec extends AbstrDockerCmdExec<BuildImageCmd, InputStream> implements BuildImageCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BuildImageCmdExec.class);
	
	public BuildImageCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected InputStream execute(BuildImageCmd command) {
	    WebResource webResource = getBaseResource().path("/build").build();
		
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
				.accept(MediaType.TEXT_PLAIN)
				.entity(command.getTarInputStream(), "application/tar")
				.post(InputStream.class);
		
	}

}
