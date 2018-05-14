package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.command.AuthCmd;
import com.github.dockerjava.api.exception.UnauthorizedException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.AuthResponse;

/**
 *
 * Authenticate with the server, useful for checking authentication.
 *
 */
public class AuthCmdImpl extends AbstrDockerCmd<AuthCmd, AuthResponse> implements AuthCmd {

    private AuthConfig authConfig;

    public AuthCmdImpl(AuthCmd.Exec exec, AuthConfig authConfig) {
        super(exec);
        withAuthConfig(authConfig);
    }

    @Override
    public AuthResponse exec() throws UnauthorizedException {
        return super.exec();
    }

    public AuthConfig getAuthConfig() {
        return authConfig;
    }

    public AuthCmd withAuthConfig(AuthConfig authConfig) {
        checkNotNull(authConfig, "AuthConfig was not specified");
        this.authConfig = authConfig;
        return this;
    }
}
