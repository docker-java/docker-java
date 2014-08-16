package com.github.dockerjava.client.command;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.model.AuthConfig;
import com.google.common.base.Preconditions;
import org.apache.commons.codec.binary.Base64;

public abstract class AbstrAuthCfgDockerCmd<T extends AbstrDockerCmd<T, RES_T>, RES_T> extends
		AbstrDockerCmd<T, RES_T> {

	protected AuthConfig authConfig;

	@SuppressWarnings("unchecked")
	public T withAuthConfig(AuthConfig authConfig) {
		Preconditions.checkNotNull(authConfig, "authConfig was not specified");
		this.authConfig = authConfig;
		return (T)this;
	}
	
	protected String registryAuth() throws DockerException {
		try {
			return Base64.encodeBase64String(new ObjectMapper().writeValueAsString(authConfig).getBytes());
		} catch (IOException e) {
			throw new DockerException(e);
		}
	}

}