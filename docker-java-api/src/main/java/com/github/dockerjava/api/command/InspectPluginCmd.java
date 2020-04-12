package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.NotFoundException;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

public interface InspectPluginCmd extends SyncDockerCmd<InspectPluginResponse> {
    @CheckForNull
    String getName();

    InspectPluginCmd withPluginName(@Nonnull String name);

    /**
     * @throws NotFoundException No such plugin
     */
    @Override
    InspectPluginResponse exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<InspectPluginCmd, InspectPluginResponse> {
    }

}

