package com.github.dockerjava.api.command;

import java.io.InputStream;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.exception.NotFoundException;

public interface CopyArchiveFromContainerCmd extends SyncDockerCmd<InputStream> {

    @CheckForNull
    String getContainerId();

    @CheckForNull
    String getHostPath();

    @CheckForNull
    String getResource();

    CopyArchiveFromContainerCmd withContainerId(@Nonnull String containerId);

    CopyArchiveFromContainerCmd withHostPath(String hostPath);

    CopyArchiveFromContainerCmd withResource(@Nonnull String resource);

    /**
     * Its the responsibility of the caller to consume and/or close the {@link InputStream} to prevent connection leaks.
     *
     * @throws NotFoundException
     *             No such container
     */
    @Override
    InputStream exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<CopyArchiveFromContainerCmd, InputStream> {
    }
}
