package com.github.dockerjava.jaxrs;

import static jersey.repackaged.com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.WebTarget;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.command.DockerCmd;
import com.github.dockerjava.api.command.DockerCmdExec;
import com.github.dockerjava.api.model.AuthConfig;

public abstract class AbstrDockerCmdExec<CMD_T extends DockerCmd<RES_T>, RES_T>
		implements DockerCmdExec<CMD_T, RES_T> {

	private WebTarget baseResource;

	public AbstrDockerCmdExec(WebTarget baseResource) {
		checkNotNull(baseResource,
				"baseResource was not specified");
		this.baseResource = baseResource;
	}

	protected WebTarget getBaseResource() {
		return baseResource;
	}

	protected String registryAuth(AuthConfig authConfig) {
		try {
			return Base64.encodeBase64String(new ObjectMapper()
					.writeValueAsString(authConfig).getBytes());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public RES_T exec(CMD_T command) {
		// this hack works because of ResponseStatusExceptionFilter
		RES_T result;
		try {
			result = execute(command);
			
		} catch (ProcessingException e) {
			if(e.getCause() instanceof DockerException) {
				throw (DockerException)e.getCause();
			} else {
				throw e;
			}
		} finally {
			try {
				command.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		return result;
	}

	protected abstract RES_T execute(CMD_T command);
}
