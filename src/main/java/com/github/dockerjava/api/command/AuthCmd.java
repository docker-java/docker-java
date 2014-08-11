package com.github.dockerjava.api.command;

import com.github.dockerjava.api.UnauthorizedException;

/**
 * 
 * Authenticate with the server, useful for checking authentication.
 * 
 */
public interface AuthCmd extends DockerCmd<Void> {
	
	@Override
	public Void exec() throws UnauthorizedException;

}