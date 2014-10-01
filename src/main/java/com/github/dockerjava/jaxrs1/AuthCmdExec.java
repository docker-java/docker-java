package com.github.dockerjava.jaxrs1;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.UnauthorizedException;
import com.github.dockerjava.api.command.AuthCmd;
import com.sun.jersey.api.client.WebResource;

public class AuthCmdExec extends AbstrDockerCmdExec<AuthCmd, Void> implements AuthCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AuthCmdExec.class);
	
	public AuthCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected Void execute(AuthCmd command) {
	    WebResource webResource = getBaseResource().path("/auth").build();
		LOGGER.trace("POST: {}", webResource);
		Response response = webResource
		        .accept(MediaType.APPLICATION_JSON)
		        .entity(command.getAuthConfig(), MediaType.APPLICATION_JSON)
		        .post(Response.class);
		
		if(response.getStatus() == 401) {
			throw new UnauthorizedException("Unauthorized");
		};
		
		return null;
	}

}
