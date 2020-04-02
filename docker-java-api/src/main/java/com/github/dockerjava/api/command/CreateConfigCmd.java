package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.model.ConfigSpec;

import javax.annotation.CheckForNull;

/**
 * Command to create a new config
 *
 * @since {@link RemoteApiVersion#VERSION_1_30}
 */
public interface CreateConfigCmd extends SyncDockerCmd<CreateConfigResponse> {

    @CheckForNull
    ConfigSpec getConfigSpec();

    CreateConfigCmd withConfigSpec(ConfigSpec configSpec);

    /**
     * @throws ConflictException Named secret already exists
     */
    @Override
    CreateConfigResponse exec() throws ConflictException;

    interface Exec extends DockerCmdSyncExec<CreateConfigCmd, CreateConfigResponse> {
    }

}
