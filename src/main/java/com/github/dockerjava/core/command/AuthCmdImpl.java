package com.github.dockerjava.core.command;

import com.github.dockerjava.api.UnauthorizedException;
import com.github.dockerjava.api.command.AuthCmd;
import com.github.dockerjava.api.command.DockerCmdExec;
import com.github.dockerjava.api.model.AuthConfig;

/**
 * 
 * Authenticate with the server, useful for checking authentication.
 * 
 */
public class AuthCmdImpl extends AbstrAuthCfgDockerCmd<AuthCmd, Void> implements AuthCmd {

	public AuthCmdImpl(DockerCmdExec<AuthCmd, Void> exec, AuthConfig authConfig) {
		super(exec);
		withAuthConfig(authConfig);
	}
	
	@Override
	public Void exec() throws UnauthorizedException {
		return super.exec();
	}
	
	@Override
	public String toString() {
		return "authenticate using " + this.getAuthConfig();
	}
}
