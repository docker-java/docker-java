package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.NotFoundException;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Remove a service.
 */
public interface RemoveServiceCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    String getServiceId();

    RemoveServiceCmd withServiceId(@Nonnull String serviceId);

    /**
     * @throws NotFoundException
     *             No such service
     */
    @Override
    Void exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<RemoveServiceCmd, Void> {
    }

}
