package com.github.dockerjava.api.command;

import java.io.InputStream;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.exception.NotFoundException;

/**
 * Export the contents of a container as a tar archive.
 */
public interface ExportContainerCmd extends SyncDockerCmd<InputStream> {

    @CheckForNull
    String getContainerId();

    ExportContainerCmd withContainerId(@Nonnull String containerId);

    /**
     * Its the responsibility of the caller to consume and/or close the {@link InputStream} to prevent connection leaks.
     *
     * @throws NotFoundException
     *             No such container
     */
    @Override
    InputStream exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<ExportContainerCmd, InputStream> {
    }
}
