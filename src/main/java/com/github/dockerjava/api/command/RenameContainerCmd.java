package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.core.RemoteApiVersion;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * rename a container.
 *
 * @since {@link RemoteApiVersion#VERSION_1_22}
 */
public interface RenameContainerCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    String getContainerId();

    RenameContainerCmd withContainerId(@Nonnull String containerId);

    @CheckForNull
    String getName();

    RenameContainerCmd withName(@Nonnull String name);


    /**
     * @throws com.github.dockerjava.api.exception.NotFoundException No such container
     */
    @Override
    Void exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<RenameContainerCmd, Void> {
    }
}
