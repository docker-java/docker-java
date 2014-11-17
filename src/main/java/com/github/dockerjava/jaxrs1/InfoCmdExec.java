package com.github.dockerjava.jaxrs1;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.InfoCmd;
import com.github.dockerjava.api.model.Info;
import com.sun.jersey.api.client.WebResource;

public class InfoCmdExec extends AbstrDockerCmdExec<InfoCmd, Info> implements InfoCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(InfoCmdExec.class);
	
	public InfoCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected Info execute(InfoCmd command) {
		WebResource webResource = getBaseResource().path("/info").build();

		LOGGER.trace("GET: {}", webResource);
		return webResource.accept(MediaType.APPLICATION_JSON).get(Info.class);
	}

}
