package com.github.dockerjava.api.command;

import java.io.InputStream;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.exception.NotFoundException;

public interface CopyFileFromContainerCmd extends SyncDockerCmd<InputStream> {

    @CheckForNull
    String getContainerId();

    @CheckForNull
    String getHostPath();

    @CheckForNull
    String getResource();

    CopyFileFromContainerCmd withContainerId(@Nonnull String containerId);

    CopyFileFromContainerCmd withHostPath(String hostPath);

    CopyFileFromContainerCmd withResource(@Nonnull String resource);

    /**
     * Its the responsibility of the caller to consume and/or close the {@link InputStream} to prevent connection leaks.
     *
     * @throws NotFoundException
     *             No such container
     */
    @Override
    InputStream exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<CopyFileFromContainerCmd, InputStream> {
    }
}
