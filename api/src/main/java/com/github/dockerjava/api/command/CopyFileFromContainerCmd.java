package com.github.dockerjava.api.command;

import java.io.InputStream;

import com.github.dockerjava.api.NotFoundException;

public interface CopyFileFromContainerCmd extends SyncDockerCmd<InputStream> {

    public String getContainerId();

    public String getResource();

    public CopyFileFromContainerCmd withContainerId(String containerId);

    public CopyFileFromContainerCmd withResource(String resource);

    public String getHostPath();

    public CopyFileFromContainerCmd withHostPath(String hostPath);

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