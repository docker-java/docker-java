package com.github.dockerjava.jaxrs1;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.VersionCmd;
import com.github.dockerjava.api.model.Version;
import com.sun.jersey.api.client.WebResource;

public class VersionCmdExec extends AbstrDockerCmdExec<VersionCmd, Version> implements VersionCmd.Exec {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(VersionCmdExec.class);

	public VersionCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected Version execute(VersionCmd command) {
		WebResource webResource = getBaseResource().path("/version").build();

		LOGGER.trace("GET: {}", webResource);
		return webResource.accept(MediaType.APPLICATION_JSON)
				.get(Version.class);
	}

}
