package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.model.SecretSpec;

import javax.annotation.CheckForNull;

/**
 * Command to create a new secret
 *
 * @since {@link RemoteApiVersion#VERSION_1_25}
 */
public interface CreateSecretCmd extends SyncDockerCmd<CreateSecretResponse> {

    @CheckForNull
    SecretSpec getSecretSpec();

    CreateSecretCmd withSecretSpec(SecretSpec secretSpec);

    /**
     * @throws ConflictException Named secret already exists
     */
    @Override
    CreateSecretResponse exec() throws ConflictException;

    interface Exec extends DockerCmdSyncExec<CreateSecretCmd, CreateSecretResponse> {
    }

}
