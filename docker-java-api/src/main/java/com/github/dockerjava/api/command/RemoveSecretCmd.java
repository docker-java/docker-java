package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.NotFoundException;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Remove a secret.
 */
public interface RemoveSecretCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    String getSecretId();

    RemoveSecretCmd withSecretId(@Nonnull String secretId);

    /**
     * @throws NotFoundException
     *             No such secret
     */
    @Override
    Void exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<RemoveSecretCmd, Void> {
    }

}
