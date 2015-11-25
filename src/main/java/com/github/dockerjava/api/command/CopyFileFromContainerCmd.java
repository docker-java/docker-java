package com.github.dockerjava.api.command;

import java.io.InputStream;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.exception.NotFoundException;

public interface CopyFileFromContainerCmd extends SyncDockerCmd<InputStream> {

    @CheckForNull
    public String getContainerId();

    @CheckForNull
    public String getHostPath();

    @CheckForNull
    public String getResource();

    public CopyFileFromContainerCmd withContainerId(@Nonnull String containerId);

    public CopyFileFromContainerCmd withHostPath(String hostPath);

    public CopyFileFromContainerCmd withResource(@Nonnull String resource);

    /**
     * Its the responsibility of the caller to consume and/or close the {@link InputStream} to prevent connection leaks.
     *
     * @throws NotFoundException
     *             No such container
     */
    @Override
    public InputStream exec() throws NotFoundException;

    public static interface Exec extends DockerCmdSyncExec<CopyFileFromContainerCmd, InputStream> {
    }
}