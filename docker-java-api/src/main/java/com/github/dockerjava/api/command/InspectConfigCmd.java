package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Config;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

public interface InspectConfigCmd extends SyncDockerCmd<Config> {

    @CheckForNull
    String getConfigId();

    InspectConfigCmd withConfigId(@Nonnull String configId);

    /**
     * @throws NotFoundException
     *             No such config
     */
    @Override
    Config exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<InspectConfigCmd, Config> {
    }
}
