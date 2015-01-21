package com.github.dockerjava.core.command;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.github.dockerjava.api.command.DockerCmd;
import com.github.dockerjava.api.command.DockerCmdExec;
import com.github.dockerjava.api.model.AuthConfig;

import com.google.common.base.Preconditions;

import org.apache.commons.codec.binary.Base64;

public abstract class AbstrAuthCfgDockerCmd<T extends DockerCmd<RES_T>, RES_T> extends
		AbstrDockerCmd<T, RES_T> {

	public AbstrAuthCfgDockerCmd(DockerCmdExec<T, RES_T> execution, AuthConfig authConfig) {
		super(execution);
		withOptionalAuthConfig(authConfig);
	}

	public AbstrAuthCfgDockerCmd(DockerCmdExec<T, RES_T> execution) {
		super(execution);
	}

	private AuthConfig authConfig;
	
	public AuthConfig getAuthConfig() {
		return authConfig;
	}

	public T withAuthConfig(AuthConfig authConfig) {
		Preconditions.checkNotNull(authConfig, "authConfig was not specified");
		return withOptionalAuthConfig(authConfig);
	}

	@SuppressWarnings("unchecked")
	private T withOptionalAuthConfig(AuthConfig authConfig) {
		this.authConfig = authConfig;
		return (T)this;
	}
	
	protected String registryAuth() {
		try {
			return Base64.encodeBase64String(new ObjectMapper().writeValueAsString(authConfig).getBytes());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}