package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.model.ConfigSpec;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Command to create a new config
 *
 * @since {@link RemoteApiVersion#VERSION_1_30}
 */
public interface UpdateConfigCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    ConfigSpec getConfigSpec();

    @CheckForNull
    String getConfigId();

    UpdateConfigCmd withConfigId(@Nonnull String configId);

    UpdateConfigCmd withConfigSpec(ConfigSpec configSpec);

    /**
     * @throws ConflictException Named config already exists
     */
    @Override
    Void exec() throws ConflictException;

    interface Exec extends DockerCmdSyncExec<UpdateConfigCmd, Void> {
    }

}
