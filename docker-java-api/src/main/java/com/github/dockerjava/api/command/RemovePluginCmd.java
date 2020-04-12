package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.NotFoundException;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

public interface RemovePluginCmd extends SyncDockerCmd<Void> {
    @CheckForNull
    String getPluginName();

    @CheckForNull
    Boolean hasForceEnabled();

    RemovePluginCmd withPluginName(@Nonnull String name);

    /**
     * force parameter to force delete of a plugin, even if it's tagged in multiple repositories
     */
    RemovePluginCmd withForce(Boolean force);

    /**
     * @throws NotFoundException No such plugin
     */
    @Override
    Void exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<RemovePluginCmd, Void> {
    }
}
