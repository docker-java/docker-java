package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.NotFoundException;

/**
 * Wait a container
 *
 * Block until container stops, then returns its exit code
 */
public interface WaitContainerCmd extends SyncDockerCmd<Integer> {

    @CheckForNull
    public String getContainerId();

    public WaitContainerCmd withContainerId(@Nonnull String containerId);

    /**
     * @throws NotFoundException
     *             container not found
     */
    @Override
    public Integer exec() throws NotFoundException;

    public static interface Exec extends DockerCmdSyncExec<WaitContainerCmd, Integer> {
    }

}