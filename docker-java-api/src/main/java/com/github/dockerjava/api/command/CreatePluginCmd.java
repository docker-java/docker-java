package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.ConflictException;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Command to create a new plugin
 *
 * @since {@link RemoteApiVersion#VERSION_1_40}
 */
public interface CreatePluginCmd extends SyncDockerCmd<CreatePluginResponse> {
    @CheckForNull
    String getName();

    /**
     * The new plugin's name. Required.
     */
    CreatePluginCmd withName(@Nonnull String name);

    /**
     * @throws ConflictException Named plugin already exists
     */
    @Override
    CreatePluginResponse exec() throws ConflictException;

    interface Exec extends DockerCmdSyncExec<CreatePluginCmd, CreatePluginResponse> {
    }
}
