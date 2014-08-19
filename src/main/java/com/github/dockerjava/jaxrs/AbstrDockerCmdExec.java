package com.github.dockerjava.jaxrs;

import java.io.IOException;

import javax.ws.rs.client.WebTarget;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.command.DockerCmd;
import com.github.dockerjava.api.command.DockerCmdExec;
import com.github.dockerjava.api.model.AuthConfig;
import com.google.common.base.Preconditions;

public abstract class AbstrDockerCmdExec<CMD_T extends DockerCmd<RES_T>, RES_T> implements DockerCmdExec<CMD_T, RES_T> {

	private WebTarget baseResource;
	
	public AbstrDockerCmdExec(WebTarget baseResource) {
		Preconditions.checkNotNull(baseResource, "baseResource was not specified");
		this.baseResource = baseResource;
	}
	
	protected WebTarget getBaseResource() {
		return baseResource;
	}
	
	protected String registryAuth(AuthConfig authConfig) {
		try {
			return Base64.encodeBase64String(new ObjectMapper().writeValueAsString(authConfig).getBytes());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
