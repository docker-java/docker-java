package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.exception.UnauthorizedException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.AuthResponse;

/**
 *
 * Authenticate with the server, useful for checking authentication.
 *
 */
public interface AuthCmd extends SyncDockerCmd<AuthResponse> {

    @CheckForNull
    AuthConfig getAuthConfig();

    AuthCmd withAuthConfig(@Nonnull AuthConfig authConfig);

    /**
     * @return The status. Based on it's value you may mean you need to authorise your account, e.g.: "Account created. Please see the
     *         documentation of the registry http://localhost:5000/v1/ for instructions how to activate it."
     * @throws UnauthorizedException
     *             If you're not authorised (e.g. bad password).
     */
    @Override
    AuthResponse exec() throws UnauthorizedException;

    interface Exec extends DockerCmdSyncExec<AuthCmd, AuthResponse> {
    }

}
