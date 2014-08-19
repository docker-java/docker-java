package com.github.dockerjava.api.command;

import com.github.dockerjava.api.UnauthorizedException;
import com.github.dockerjava.api.model.AuthConfig;

/**
 * 
 * Authenticate with the server, useful for checking authentication.
 * 
 */
public interface AuthCmd extends DockerCmd<Void> {
	
	public AuthConfig getAuthConfig();

	public AuthCmd withAuthConfig(AuthConfig authConfig);
	
	@Override
	public Void exec() throws UnauthorizedException;
	
	public static interface Exec extends DockerCmdExec<AuthCmd, Void> {
	}

}