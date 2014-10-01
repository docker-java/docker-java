package com.github.dockerjava.jaxrs1;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.PingCmd;
import com.sun.jersey.api.client.WebResource;

public class PingCmdExec extends AbstrDockerCmdExec<PingCmd, Void> implements PingCmd.Exec {

	private static final Logger LOGGER = LoggerFactory.getLogger(PingCmdExec.class);

	public PingCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected Void execute(PingCmd command) {
		WebResource webResource = getBaseResource().path("/_ping").build();
	       
        LOGGER.trace("GET: {}", webResource);
        webResource.get(Response.class);
        
        return null;
	}

}
