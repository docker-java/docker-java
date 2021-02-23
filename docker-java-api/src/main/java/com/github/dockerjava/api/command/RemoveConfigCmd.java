package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.NotFoundException;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Remove a config.
 */
public interface RemoveConfigCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    String getConfigId();

    RemoveConfigCmd withConfigId(@Nonnull String secretId);

    /**
     * @throws NotFoundException
     *             No such config
     */
    @Override
    Void exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<RemoveConfigCmd, Void> {
    }

}
