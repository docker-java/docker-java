package com.github.dockerjava.jaxrs1;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.UnauthorizedException;
import com.github.dockerjava.api.command.AuthCmd;
import com.github.dockerjava.api.model.AuthResponse;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class AuthCmdExec extends AbstrDockerCmdExec<AuthCmd, AuthResponse> implements AuthCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AuthCmdExec.class);
	
	public AuthCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected AuthResponse execute(AuthCmd command) {
	    WebResource webResource = getBaseResource().path("/auth").build();
		LOGGER.trace("POST: {}", webResource);
		ClientResponse response = webResource
		        .accept(MediaType.APPLICATION_JSON)
		        .entity(command.getAuthConfig(), MediaType.APPLICATION_JSON)
		        .post(ClientResponse.class);
		
		if(response.getStatus() == 401) {
			throw new UnauthorizedException("Unauthorized");
		};
		
		return response.getEntity(AuthResponse.class);
	}

}
