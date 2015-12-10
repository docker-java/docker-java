package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.AuthCmd;
import com.github.dockerjava.api.exception.UnauthorizedException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.AuthResponse;

/**
 *
 * Authenticate with the server, useful for checking authentication.
 *
 */
public class AuthCmdImpl extends AbstrAuthCfgDockerCmd<AuthCmd, AuthResponse> implements AuthCmd {

    public AuthCmdImpl(AuthCmd.Exec exec, AuthConfig authConfig) {
        super(exec);
        withAuthConfig(authConfig);
    }

    @Override
    public AuthResponse exec() throws UnauthorizedException {
        return super.exec();
    }
}
