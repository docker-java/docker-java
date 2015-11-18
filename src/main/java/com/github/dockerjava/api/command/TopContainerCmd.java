package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.NotFoundException;

/**
 * List processes running inside a container
 */
public interface TopContainerCmd extends SyncDockerCmd<TopContainerResponse> {

    @CheckForNull
    public String getContainerId();

    @CheckForNull
    public String getPsArgs();

    public TopContainerCmd withContainerId(@Nonnull String containerId);

    public TopContainerCmd withPsArgs(String psArgs);

    /**
     * @throws NotFoundException
     *             No such container
     */
    @Override
    public TopContainerResponse exec() throws NotFoundException;

    public static interface Exec extends DockerCmdSyncExec<TopContainerCmd, TopContainerResponse> {
    }

}